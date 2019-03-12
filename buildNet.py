#code based on Python 3.6
#encoding=utf-8
#qinxuan
#connect to database to search co-ocurrance relation between entities in certain type from pmid_list file

#based on Python 3.6
#encoding=utf-8
#qinxuan
#connect to database to search co-ocurrance relation between entities in certain type from pmid_list file

import psycopg2
from itertools import combinations

#import time
#begin =time.clock()

#pmid_list file
#documents = "30030436\n30030434\n30760519"

#entity_list file
#entities = "9606.ENSP00000365687\n9606.ENSP00000345206\n9606.ENSP00000425979\n9606.ENSP00000356213\n9606.ENSP00000466090\n9606.ENSP00000229135\n9606.ENSP00000466090\n9606.ENSP00000365687\n9606.ENSP00000365687\n9606.ENSP00000365687\n9606.ENSP00000277541\n9606.ENSP00000345206\n9606.ENSP00000425979\n9606.ENSP00000358363\n9606.ENSP00000437256\n9606.ENSP00000425979\n9606.ENSP00000437256\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000405330\n9606.ENSP00000405330\n9606.ENSP00000216037\n9606.ENSP00000250448\n9606.ENSP00000399356"


def Get_documents(documents):
        pmidList = documents.split('\\n')
        documents = str(pmidList).replace('[','').replace(']','')
        return pmidList,documents

def Get_entities(entities):
        entityList = entities.split('\\n')
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


def Get_result(documents,entities):
        documents=str(documents)
        entities=str(entities)
        #documents = "30030436\n30030434\n30760519"
        #entities = "9606.ENSP00000365687\n9606.ENSP00000345206\n9606.ENSP00000425979\n9606.ENSP00000356213\n9606.ENSP00000466090\n9606.ENSP00000229135\n9606.ENSP00000466090\n9606.ENSP00000365687\n9606.ENSP00000365687\n9606.ENSP00000365687\n9606.ENSP00000277541\n9606.ENSP00000345206\n9606.ENSP00000425979\n9606.ENSP00000358363\n9606.ENSP00000437256\n9606.ENSP00000425979\n9606.ENSP00000437256\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000358363\n9606.ENSP00000405330\n9606.ENSP00000405330\n9606.ENSP00000216037\n9606.ENSP00000250448\n9606.ENSP00000399356"
        result_relation={}
        sentences_database={}
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
                        while et < len(entityStops):
                                entityStart,entityStop = entityStops[et]
                                sentenceStart,sentenceStop=sentencelocation[st]
                                if entityStop < sentenceStop:
                                        et = et+1
                                        sentenceEntityList.add(str(entity_location[pmid][(entityStart,entityStop)][0])+'.'+entity_location[pmid][(entityStart,entityStop)][1])
                                else:
                                        if len(sentenceEntityList)>2:
                                                        #sentenceEntityList: entity ID list from One sentence 'st'
                                                relations=combinations(sentenceEntityList,2)
                                                sentenceContent=abstracts[pmid][sentenceStart:sentenceStop]
                                                sentenceID=str(pmid)+'.'+str(st)
                                                sentences_database[sentenceID]=sentenceContent
                                                for node in relations:
                                                        if node not in result_relation.keys():
                                                                result_relation[node]=[sentenceID]
                                                        else:
                                                                result_relation[node].append(sentenceID)
                                        st=st+1
                                        sentenceEntityList=set()
                                        if st == len(sentencelocation):
                                                break
        edges=[]
        nodes=set()
        for node in result_relation.keys():
                edges.append({"source":node[0],"target":node[1],"sentences":result_relation[node]})
                nodes.update([node[0]])
                nodes.update([node[1]])
        nodes=list(nodes)
        result={"edges":edges,"nodes":nodes,"sentences":sentences_database}
        return result

