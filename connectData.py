# -*- coding: utf-7 -*-
"""
Created on Fri Feb 22 00:44:31 2019

@author: xqin
"""

#code based on Python 3.6
#encoding=utf-8
#qinxuan
#connect to server to abtain the entities and pmidlist

from flask import Flask,jsonify,request
import buildnet
import time

app = Flask(__name__)

@app.route('/getdata', methods=['POST','GET'])
def index():
        start_time=time.time()
        documents = request.form.get("documents", False)
#documents = request.form['documents']
#       entities = request.form['entities']
        entities= request.form.get("entities", False)
        post_time = time.time()
        result=buildnet.Get_result(documents,entities)
        running_time=time.time()
        result["post time"]=str(post_time-start_time)
        result["running time"]=str(running_time-post_time)
        result["app time"]=str(running_time-start_time)
        return jsonify(result)


if __name__=='__main__':
    app.run(debug=True, host='0.0.0.0',port='9000')

