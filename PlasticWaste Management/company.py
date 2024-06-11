from flask import *
from database import *
import uuid
company=Blueprint('company',__name__)
@company.route('/companyhome',methods=['get','post'])
def companyhome():
	return render_template('companyhome.html')
@company.route('/companycollectwaste',methods=['get','post'])
def companycollectwaste():
	data={}
	ids=session['log_id']
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None
	if action=="update":
		q="select *,concat(fname,' ',lname)as NAME from company_collection inner join agents using(agent_id) where collection_id='%s'"%(id)
		res=select(q)
		data['updateprt']=res
	if 'update' in request.form:
		waste=request.form['quan']
		q="update company_collection set waste_quantity='%s' where collection_id='%s'"%(waste,id)
		update(q)
		return redirect(url_for('company.companycollectwaste'))
	if action=="delete":
		q="delete from company_collection where collection_id='%s'"%(id)
		delete(q)
		return redirect(url_for('company.companycollectwaste'))			
	if 'submit' in request.form:
		agent=request.form['aname']
		waste=request.form['quan']
		q="insert into company_collection values(null,(select com_id from company where log_id='%s'),'%s','%s',Curdate())"%(ids,agent,waste)
		insert(q)
	q="select *,concat(fname,' ',lname)as NAME from agents"	
	res=select(q)
	data['agent']=res
	q="select *,concat(fname,' ',lname)as NAME from company_collection inner join agents using(agent_id)inner join company using(com_id)"
	res=select(q)
	data['waste']=res
	return render_template('companycollect_waste.html',data=data)

@company.route('/companymanageproducts',methods=['get','post'])
def companymanageproducts():
	data={}
	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None
	if action=="update":
		q="select * from products where pro_id='%s'"%(id)
		res=select(q)
		data['updateprt']=res
	if 'update' in request.form:
		amount=request.form['amount']
		q="update products set amount='%s' where pro_id='%s'"%(amount,id)
		update(q)
		return redirect(url_for('company.companymanageproducts'))
	if action=="delete":
		q="delete from products where pro_id='%s'"%(id)
		delete(q)
		return redirect(url_for('company.companymanageproducts'))				
	if 'submit' in request.form:
		proname=request.form['pname']
		des=request.form['des']
		amount=request.form['amount']
		image=request.files['image']
		path='static/uploads/'+str(uuid.uuid4())+image.filename
		image.save(path)
		q="insert into products values(null,'%s','%s','%s','%s')"%(proname,des,amount,path)
		insert(q)
	q="select * from products"
	res=select(q)
	data['pro']=res	
	return render_template('companymanage_product.html',data=data)
@company.route('/companymanagestock',methods=['get','post'])
def companymanagestock():
	data={}
	id=request.args['id']
	data['id']=id
	# print(data['id'])
	ids=session['log_id']
	q="select  * from products where pro_id='%s'"%(id)
	print("query",q)
	res=select(q)
	data['updateprt']=res
	if 'action' in request.args:
		action=request.args['action']
		id1=request.args['id1']
	else:
		action=None
	if action=="update":
		q="select * from stock inner join products using(pro_id) where stock_id='%s'"%(id1)
		res=select(q)
		data['updatepart']=res
		data['id']=id
		# data['updateprt']=res	
	if 'update' in request.form:
		availability=request.form['aval']
		q="update stock set availability='%s' where stock_id='%s'"%(availability,id1)
		update(q)
		return redirect(url_for('company.companymanagestock',id=id))
	if action=="delete":
		q="delete from stock where stock_id='%s'"%(id1)
		delete(q)
		return redirect(url_for("company.companymanagestock",id=id))	
	if 'submit' in request.form:
		availability=request.form['aval']
		q="insert into stock values(null,(select com_id from company where log_id='%s'),'%s','%s',curdate())"%(ids,id,availability)	
		insert(q)		
	q="select * from stock inner join company using(com_id) inner join products using(pro_id)"
	res=select(q)
	data['sto']=res
	# data['id']=res
	return render_template('companymanagestock.html',data=data)			

@company.route('/companyvieworders',methods=['get','post'])
def companyvieworders():
	data={}
	q="SELECT *,CONCAT(fname,' ',lname)AS NAME FROM purchase_details INNER JOIN purchase_master USING(pm_id) INNER JOIN users USING(user_id)INNER JOIN company USING(com_id) INNER JOIN stock USING(stock_id)INNER JOIN products USING(pro_id)"
	res=select(q)
	data['order']=res	
	return render_template('companyview_orders.html',data=data)
@company.route('/accept',methods=['get','post'])
def accept():
	data={}
	if 'id' in request.args:
		id=request.args['id']
		q="update purchase_master set delivery_status='Delivered' where delivery_status='ordered' and pm_id='%s'"%(id)
		update(q)
		return redirect(url_for('company.company_assign_agent',id=id))
	return render_template('companyview_orders.html',data=data)
@company.route('/reject',methods=['get','post'])
def reject():
	data={}
	if 'id1' in request.args:
		id1=request.args['id1']
		q="update purchase_master set delivery_status='Reject' where delivery_status='ordered' and pm_id='%s'"%(id1)
		update(q)
		return redirect(url_for('company.companyvieworders'))
	return render_template('companyview_orders.html',data=data)




@company.route('/company_assign_agent',methods=['get','post'])
def company_assign_agent():
	data={}
	id=request.args['id']
	q="SELECT *,CONCAT(`fname`,' ',`lname`) AS ag_name FROM `agents`"
	res=select(q)
	data['asgn_agent']=res

	if 'assign_agent' in request.form:
		agent_id=request.form['agent_id']
		q="UPDATE `purchase_master` SET `assigned_agent`='%s' WHERE `pm_id`='%s'"%(agent_id,id)
		update(q)

		return redirect(url_for('company.companyvieworders'))
	return render_template('company_assign_agent.html',data=data)

