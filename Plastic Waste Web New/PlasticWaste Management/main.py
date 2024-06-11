from flask import Flask
from admin import admin
from public import public
from company import company
from api import api
app=Flask(__name__)
app.secret_key="bency"
app.register_blueprint(public)
app.register_blueprint(admin,url_prefix='/admin')
app.register_blueprint(company,url_prefix='/company')
app.register_blueprint(api,url_prefix='/api')
app.run(debug=True,port=5019,host="0.0.0.0")