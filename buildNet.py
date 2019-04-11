# -*- coding: utf-8 -*-
"""
Created on Mon Apr  1 20:49:19 2019

@author: xqin
"""

#code based on Python 3.6
#encoding=utf-8
#qinxuan
#connect to database to search co-ocurrance relation between entities in certain type from pmid_list file

#based on Python 3.6
#encoding=utf-8
#qinxuan
#connect to database to search co-ocurrance relation between entities in certain type from pmid_list file

import psycopg2
import re
from itertools import combinations

#import time
#begin =time.clock()

#pmid_list file
documents = "30030436\n30030434\n30760519"

#entity_list file
entities = "9606.ENSP00000365687\n9606.ENSP00000345206\n9606.ENSP00000425979\n9606.ENSP00000356213\n9606.ENSP00000466090\n9606.ENSP00000229135\n9606.ENSP00000466090\n9606.ENSP00000365687\n9606.ENSP00000365687\n9606.ENSP00000365687\n9606.ENSP00000277541\n9606.ENSP00000345206\n9606.ENSP00000425979\n9606.ENSP00000358363\n9606.ENSP00000437256\n9606.ENSP00000425979\n9606.ENSP00000437256\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000405330\n9606.ENSP00000405330\n9606.ENSP00000216037\n9606.ENSP00000250448\n9606.ENSP00000399356"


def Get_documents(documents):
        pmidList = re.split("\s+",documents)
        documents = str(pmidList).replace('[','').replace(']','')
        return pmidList,documents

def Get_entities(entities):
        entityList = re.split("\s+",entities)
        for i in range(len(entityList)):
                entityList[i] = entityList[i].split('.')
                entities = str(entityList).replace("], [","),(").replace("[[","(").replace("]]",")")
        return entityList,entities





'''
return
entityStop[pmid]={type1.id1:entityStop1,type2.id2:entityStop2}
'''

def Get_entity_location(conn,documents,entities):
        cur = conn.cursor()
        sql = "SELECT * FROM matches where document in(%s) and (type,id) in(%s);" % (documents,entities)
        cur.execute(sql)
        rows = cur.fetchall()
        entity_location={}
        for row in rows:
                if row[0] in entity_location.keys():
                        entity_location[row[0]][(row[1],row[2])]= (row[3],row[4])
                else:
                        entity_location[row[0]]={}
                        entity_location[row[0]][(row[1],row[2])]= (row[3],row[4])
        return entity_location


'''
Get_sentenceStop(documents)
return
sentenceStop[pmid]=[sentence1StopLocation,sentence2StopLocation,...,sentenceNStopLocation]
'''
def Get_sentence_location(conn,documents):
        cur = conn.cursor()
        sql="select * from segments where document in(%s);" % (documents)
        cur.execute(sql)
        rows = cur.fetchall()
        sentence_location = {}
        for row in rows:
                if row[0] not in sentence_location.keys():
                        sentence_location[row[0]] = [(row[1],row[2])]
                else:
                        sentence_location[row[0]].append((row[1],row[2]))
        return sentence_location

def match_entity_name(nodes):
	con = psycopg2.connect(host = "localhost", port = "5432",dbname = "dictionary", user = "guest",password = "")
	cur = con.cursor()
	node_list={}
	for node in nodes:
		type_id = node.split('.')
		node_list[node] = {"name":type_id[1],"type_id":node,"type":type_id[0],"id":type_id[1]}
	entiti=[]
	for i in range(len(nodes)):
		nodes[i] = nodes[i].split('.')
		entities = str(nodes).replace("], [","),(").replace("[[","(").replace("]]",")")
	sql="select * from preferred where (type,id) in(%s);" % (entities)
	cur.execute(sql)
	rows = cur.fetchall()
	for row in rows:
		node_list[str(row[0])+'.'+row[1]]={"name":row[2],"type_id":str(row[0])+'.'+row[1],"type":row[0],"id":row[1]}
	con.close()
	return node_list
        

def Get_abstracts(conn,documents):
        try:
                cur = conn.cursor()
                cur.execute("select document,text from documents where document in(%s)" % (documents))
                rows=cur.fetchall()
        except:
                conn.set_client_encoding('UTF8')
                cur = conn.cursor()
                cur.execute("select document,text from documents where document in(%s)" % (documents))
                rows=cur.fetchall()
        abstracts={}
        for i in range(len(rows)):
                abstracts[rows[i][0]]=rows[i][1]
        return abstracts


class sentenceID:
    def __init__(self, pmid,st,entityLocation1,entityLocation2):
        self.pmid = pmid
        self.st = st
        self.entityLocation1 = entityLocation1
        self.entityLocation2 = entityLocation2


def Get_result(documents,entities):
	documents=str(documents)
	entities=str(entities)
	result_relation={}
	sentences_database={}
	pmids=set()
	pmidList,documents = Get_documents(documents)
	entityList,entities = Get_entities(entities)
	#connect to database
	conn = psycopg2.connect(host = "localhost", port = "5432",dbname = "textmining", user = "guest",password = "")
	sentenceStop_dic = Get_sentence_location(conn,documents)
	entity_location = Get_entity_location(conn,documents,entities)
	abstracts = Get_abstracts(conn,documents)
	for pmid in entity_location.keys():
		sentenceEntityList=set()
		if len(entity_location[pmid])>2:
		#to compare entity and sentence location in same pmid
			entityStops = list(entity_location[pmid].keys())
			sentencelocation = sentenceStop_dic[pmid]
			et = 0
			st = 0
			dic={}
			while et < len(entityStops):
				entityStart,entityStop = entityStops[et]
				sentenceStart,sentenceStop=sentencelocation[st]
				if entityStop < sentenceStop:
					et = et+1
					entityLoction=[entityStart-sentenceStart,entityStop-sentenceStart+1]
					entityID=str(entity_location[pmid][(entityStart,entityStop)][0])+'.'+entity_location[pmid][(entityStart,entityStop)][1]
					sentenceEntityList.add(entityID)
					if entityID not in dic.keys():
						dic[entityID]=[entityLoction]
					else:
						dic[entityID].append(entityLoction)
				else:
					if len(sentenceEntityList)>2:
						pmids.add(str(pmid))
						#sentenceEntityList: entity ID list from One sentence 'st'
						relations=combinations(sentenceEntityList,2)
						sentenceContent=abstracts[pmid][sentenceStart:sentenceStop]
						sentenceID=str(pmid)+'.'+str(st)
						sentences_database[sentenceID]=sentenceContent
						for node in relations:
							if (node[0],node[1]) not in result_relation.keys():
								if (node[1],node[0]) not in result_relation.keys():
									result_relation[(node[0],node[1])]=[{"sentenceID":sentenceID,"entityLocation1":dic[node[0]],"entityLocation2":dic[node[1]]}]
								else:
									result_relation[(node[1],node[0])].append({"sentenceID":sentenceID,"entityLocation1":dic[node[1]],"entityLocation2":dic[node[0]]})
							else:
								result_relation[(node[0],node[1])].append({"sentenceID":sentenceID,"entityLocation1":dic[node[0]],"entityLocation2":dic[node[1]]})
					dic={}                                        
					st=st+1
					sentenceEntityList=set()
					if st == len(sentencelocation):
						break
	#{('9606.ENSP00000358363', '9606.ENSP00000425979'): [['30030436.6', [[180, 186], [189, 198]], [[48, 53]]]], ('9606.ENSP00000358363', '9606.ENSP00000437256'): [['30030436.6', [[180, 186], [189, 198]], [[75, 80]]]], ('9606.ENSP00000425979', '9606.ENSP00000437256'): [['30030436.6', [[48, 53]], [[75, 80]]]]}
	conn.close()
	edges=[]
	nodes=set()
	for node in result_relation.keys():
		edges.append({"source":node[0],"target":node[1],"sentences":result_relation[node]})
		nodes.add(node[0])
		nodes.add(node[1])
	nodes=list(nodes)
	nodes_list=match_entity_name(nodes)
	result={"edges":edges,"nodes":nodes_list,"sentences":sentences_database,"pmids":list(pmids)}
	return result
#{'edges': [{'source': '9606.ENSP00000275493', 'target': '9606.ENSP00000407586', 'sentences': [{'sentenceID': '30893511.4', 'entityLocation1': [[246, 249]], 'entityLocation2': [[234, 237]]}]}, {'source': '9606.ENSP00000275493', 'target': '9606.ENSP00000451828', 'sentences': [{'sentenceID': '30542713.13', 'entityLocation1': [[41, 44]], 'entityLocation2': [[55, 57]]}]}, {'source': '9606.ENSP00000407586', 'target': '9606.ENSP00000451828', 'sentences': [{'sentenceID': '30181243.5', 'entityLocation1': [[71, 74], [92, 95], [214, 217]], 'entityLocation2': [[253, 255]]}]}, {'source': '9606.ENSP00000385675', 'target': '9606.ENSP00000407586', 'sentences': [{'sentenceID': '26762195.5', 'entityLocation1': [[141, 143]], 'entityLocation2': [[112, 115]]}]}, {'source': '9606.ENSP00000385675', 'target': '9606.ENSP00000264657', 'sentences': [{'sentenceID': '30420046.10', 'entityLocation1': [[169, 172]], 'entityLocation2': [[93, 97]]}]}, {'source': '9606.ENSP00000407586', 'target': '9606.ENSP00000264657', 'sentences': [{'sentenceID': '27003603.4', 'entityLocation1': [[207, 210]], 'entityLocation2': [[0, 4]]}]}], 'nodes': {'9606.ENSP00000358363': {'name': 'PDE4DIP', 'type_id': '9606.ENSP00000358363', 'type': 9606, 'id': 'ENSP00000358363'}, '9606.ENSP00000425979': {'name': 'DROSHA', 'type_id': '9606.ENSP00000425979', 'type': 9606, 'id': 'ENSP00000425979'}, '9606.ENSP00000437256': {'name': 'DICER1', 'type_id': '9606.ENSP00000437256', 'type': 9606, 'id': 'ENSP00000437256'}}, 'sentences': {'30030436.6': 'We identified recurrent homozygous deletions of DROSHA, acting upstream of DICER1 in microRNA processing, and a novel microduplication involving chromosomal region 1q21 containing PDE4DIP (myomegalin), comprising the ancient DUF1220 protein domain.'}}

