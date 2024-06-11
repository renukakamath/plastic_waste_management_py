from flask import *
from database import *
admin=Blueprint('admin',__name__)
@admin.route('/adminhome',methods=['get','post'])
def adminhome():
	return render_template('adminhome.html')
@admin.route('/admanageagents',methods=['get','post'])
def admanageagents():
	data={}
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
		id1=request.args['id1']
	else:
		action=None
	if action=="update":
		q="select * from agents inner join login using(log_id) where agent_id='%s'"%(id)
		res=select(q)
		data['updateprt']=res
	if 'update' in request.form:
		phone=request.form['phone']
		email=request.form['email']
		q="update agents set phone='%s',email='%s' where agent_id='%s'"%(phone,email,id)
		update(q)
		return redirect(url_for('admin.admanageagents'))
	if action=="delete":
		q="delete from agents where agent_id='%s'"%(id)
		delete(q)
		q="delete from login where log_id='%s'"%(id1)
		delete(q)
		return redirect(url_for('admin.admanageagents'))				
	if 'submit' in request.form:
		fname=request.form['fname']
		lname=request.form['lname']
		phone=request.form['phone']
		email=request.form['email']
		username=request.form['username']
		password=request.form['password']
		q="insert into login values(null,'%s','%s','Agent')"%(username,password)
		res=insert(q)
		q="insert into agents values(null,'%s','%s','%s','%s','%s')"%(res,fname,lname,phone,email)
		insert(q)
	q="select *,concat(fname,' ',lname)as NAME from agents"
	res=select(q)
	data['agent']=res	
	return render_template('admanageagents.html',data=data)
@admin.route('/admanagecompany',methods=['get','post'])
def admanagecompany():
	data={}
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
		id1=request.args['id1']
	else:
		action=None
	if action=="update":
		q="select * from company inner join login using(log_id) where com_id='%s'"%(id)
		res=select(q)
		data['updateprt']=res
		data['id1']=res
	if 'update' in request.form:
		phone=request.form['phone']
		email=request.form['email']
		q="update company set phone='%s',email='%s' where com_id='%s'"%(phone,email,id)
		update(q)
		return redirect(url_for('admin.admanagecompany',id1=id1))
	if action=="delete":
		q="delete from company where com_id='%s'"%(id)
		delete(q)
		q="delete from login where log_id='%s'"%(id1)
		delete(q)
		return redirect(url_for('admin.admanagecompany'))				
	if 'submit' in request.form:
		cname=request.form['cname']
		phone=request.form['phone']
		email=request.form['email']
		username=request.form['username']
		password=request.form['password']
		q="insert into login values(null,'%s','%s','company')"%(username,password)
		res=insert(q)
		q="insert into company values(null,'%s','%s','%s','%s')"%(res,cname,phone,email)
		insert(q)
	q="select * from company"
	res=select(q)
	data['com']=res	
	return render_template('admanagecompany.html',data=data)	
@admin.route('/adviewusers',methods=['get','post'])
def adviewusers():
	data={}
	q="select *,concat(fname,' ',lname)as NAME from users"
	res=select(q)
	data['user']=res	
	return render_template('adviewusers.html',data=data)
@admin.route('/adviewfeedback',methods=['get','post'])	
def adviewfeedback():
	data={}
	q="select *,concat(fname,' ',lname)as NAME from feedback inner join users using(user_id)"
	res=select(q)
	data['feed']=res
	return render_template('adviewfeedback.html',data=data)
@admin.route('/admanageroutes',methods=['get','post'])
def admanageroutes():
	data={}
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None	
	if action=="update":
		q="select * from routes where route_id='%s'"%(id)
		res=select(q)
		data['updateprt']=res
	if 'update' in request.form:
		rname=request.form['rname']
		rdes=request.form['rdes']	
		q="update routes set route_name='%s',route_des='%s' where route_id='%s'"%(rname,rdes,id)
		update(q)
		return redirect(url_for('admin.admanageroutes'))
	if action=="delete":
		q="delete from routes where route_id='%s'"%(id)
		delete(q)
		return redirect(url_for('admin.admanageroutes'))	
	if 'submit' in request.form:
		rname=request.form['rname']
		rdes=request.form['rdes']
		q="insert into routes values(null,'%s','%s')"%(rname,rdes)
		insert(q)
	q="select * from routes"
	res=select(q)
	data['route']=res	
	return render_template('admanageroutes.html',data=data)
@admin.route('/adassignroutes',methods=['get','post'])
def adassignroutes():
	data={}
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None	
	if action=="update":
		q="select * from route_assign where assign_id='%s'"%(id)
		res=select(q)
		data['updateprt']=res
		q="select route_id,route_name,(route_id='%s')as routes from routes order by routes desc,route_id asc"%(res[0]['route_id'])
		res1=select(q)
		data['updateroute']=res1
		q="SELECT agent_id,fname,lname,(agent_id='%s')AS agent FROM agents ORDER BY  agent DESC,agent_id ASC"%(res[0]['agent_id'])
		res1=select(q)
		data['updateagent']=res1		
	if 'submit' in request.form:
		aname=request.form['aname']
		rname=request.form['rname']
		q="insert into route_assign values(null,'%s','%s',Curdate())"%(aname,rname)
		insert(q)
	q="select *,concat(fname,' ',lname)as name from agents"
	res=select(q)
	data['agent']=res	
	q="select * from routes"
	res=select(q)
	data['route']=res
	q="select *,concat(fname,' ',lname)as NAME from route_assign inner join agents using(agent_id) inner join routes using(route_id)"
	res=select(q)
	data['assign']=res
	return render_template('adassignroutes.html',data=data)

@admin.route('/adtrackagent',methods=['get','post'])
def adtrackagent():
	data={}
	q="select *,concat(fname,' ',lname)as NAME from agents"
	res=select(q)
	data['track']=res
	return render_template('adtrack_agents.html',data=data)

@admin.route('/advieworders',methods=['get','post'])
def advieworders():
	data={}
	q="SELECT *,CONCAT(fname,' ',lname)AS NAME FROM purchase_details INNER JOIN purchase_master USING(pm_id) INNER JOIN users USING(user_id)INNER JOIN company USING(com_id) INNER JOIN stock USING(stock_id)INNER JOIN products USING(pro_id)"
	res=select(q)
	data['order']=res
	return render_template('adviewpurchase.html',data=data)	

@admin.route('/adviewpay',methods=['get','post'])
def adviewpay():
	data={}
	q="select *,concat(fname,' ',lname)as NAME from payment inner join users using(user_id)"
	res=select(q)
	data['pay']=res
	return render_template('adviewpayment.html',data=data)	
























