
from flask import *
from database import*
import uuid
import demjson
api=Blueprint('api',__name__)
@api.route('/login/',methods=['get','post'])
def login():
	data={}
	username = request.args['username']
	password = request.args['password']
	
	q="SELECT * FROM `login` WHERE `username`='%s' AND `password`='%s'"%(username,password)
	res = select(q)
	if(len(res) > 0):
		data['status']  = 'success'
		data['data'] = res
	else:
		data['status']	= 'failed'
	return  demjson.encode(data)

@api.route('/register/',methods=['get','post'])
def register():
	data={}
	fname=request.args['firstname']
	lname=request.args['lastname']
	phone=request.args['phone']
	email=request.args['email']
	hname=request.args['hname']
	place=request.args['place']
	pincode=request.args['pincode']
	uname=request.args['uname']
	passw=request.args['pass']
	q="Insert into login values (null,'%s','%s','user')"%(uname,passw)
	ids=insert(q)
	print(q)
	q="INSERT INTO `users` VALUES(NULL,'%s','%s','%s','%s','%s','%s','%s','%s')"%(ids,fname,lname,phone,email,hname,place,pincode)
	print(q) 
	insert(q)
	
	data['status']="Success"
	data['method']="register"
	return demjson.encode(data)

@api.route('/send_feedback/',methods=['get','post'])
def send_feedback():
	data={}
	feedback=request.args['Feedback']
	log_id=request.args['log_id']

	q="INSERT INTO `feedback` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `log_id`='%s'),'%s',CURDATE())"%(log_id,feedback)
	insert(q)

	data['status']="success"
	data['method']="send_feedback"
	return demjson.encode(data)

@api.route('/view_feedback/',methods=['get','post'])
def view_feedback():
	data={}
	log_id=request.args['logid']

	q="SELECT * FROM `feedback` WHERE `user_id`=(SELECT `user_id` FROM `users` WHERE `log_id`='%s')"%(log_id)
	print(q)
	res=select(q)
	print(res)
	if res:
		data['status']="Success"
		data['data']=res

	else:
		data['status']= 'failed'
	data['method']  = 'view_feedback'
	return demjson.encode(data)

@api.route('/view_routes/',methods=['get','post'])
def view_routes():
	data={}
	log_id=request.args['logid']

	q="SELECT * FROM `route_assign` INNER JOIN `routes` USING(`route_id`) INNER JOIN `agents` USING(`agent_id`) INNER JOIN `collection_request` USING(`route_id`) WHERE `agent_id`=(SELECT `agent_id` FROM `agents` WHERE `log_id`='%s')"%(log_id)
	print(q)
	res=select(q)
	if res:
		data['status']="Success"
		data['data']=res
	else:
		data['status']= 'failed'
	data['method']  = 'view_routes'
	return demjson.encode(data)


@api.route('/user_view_product/',methods=['get','post'])
def user_view_product():
	data={}
	# log_id=request.args['logid']

	q="SELECT * FROM `products` INNER JOIN `stock` USING(`pro_id`) INNER JOIN `company` USING(`com_id`)"
	print(q)
	res=select(q)
	if res:
		data['status']="Success"
		data['data']=res

	else:
		data['status']= 'failed'
	# data['method']  = 'user_view_product'
	return demjson.encode(data)


@api.route('/user_order_product/',methods=['get','post'])
def user_order_product():
	data={}
	t_price=request.args['t_price']
	log_id=request.args['log_id']
	com_id=request.args['company_id']
	stock_id=request.args['stock_id']
	amount=request.args['amount']
	quantity=request.args['quantity']

	q="INSERT INTO `purchase_master` VALUES(NULL,(select user_id from users where log_id='%s'),'%s','%s',CURDATE(),'NA','ordered')"%(log_id,com_id,t_price)
	pm_id=insert(q)

	q="INSERT INTO  `purchase_details` VALUES(NULL,'%s','%s','%s','%s')"%(pm_id,stock_id,quantity,amount)
	insert(q)

	data['status']="Success"
	data['method']="user_order_product"
	return demjson.encode(data)


@api.route('/user_view_routes/',methods=['get','post'])
def user_view_routes():
	data={}
	# log_id=request.args['logid']

	q="SELECT * FROM `routes` "
	print(q)
	res=select(q)
	if res:
		data['status']="Success"
		data['data']=res

	else:
		data['status']= 'failed'
	data['method']  = 'user_view_routes'
	return demjson.encode(data)

@api.route('/user_collection_request/',methods=['get','post'])
def user_collection_request():
	data={}
	lati=request.args['lati']
	longi=request.args['longi']
	log_id=request.args['logid']
	route_id=request.args['route_id']
	quantity=request.args['quantity']

	q="INSERT INTO `collection_request` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `log_id`='%s'),'%s','%s','%s',CURDATE(),'Pending')"%(log_id,route_id,lati,longi)
	rqst_id=insert(q)
	# q="INSERT INTO `user_waste_collection` VALUES(NULL,'%s',(SELECT `agent_id` FROM `route_assign` WHERE `route_id`='%s'),CURDATE(),'%s')"%(rqst_id,route_id,quantity)
	q="INSERT INTO `user_waste_collection` VALUES(NULL,'%s','0',CURDATE(),'%s')"%(rqst_id,quantity)
	insert(q)

	data['status']="Success"
	data['method']="user_collection_request"
	return demjson.encode(data)


@api.route('/user_view_product_details/',methods=['get','post'])
def user_view_product_details():
	data={}
	pro_id=request.args['pro_id']
	# company_id=request.args['company_id']
	# stock_id=request.args['stock_id']
	# log_id=request.args['log_id']
	# route_id=request.args['route_id']

	q="SELECT * FROM `stock` INNER JOIN `company` USING(`com_id`) INNER JOIN `products` USING(`pro_id`) WHERE `pro_id`='%s'"%(pro_id)
	res=select(q)
	data['data']=res
	data['status']="Success"
	data['method']="user_view_product_details"
	return demjson.encode(data)

@api.route('/agent_confirm_request/',methods=['get','post'])
def agent_confirm_request():
	data={}
	rqid=request.args['rqid']
	# company_id=request.args['company_id']
	# stock_id=request.args['stock_id']
	# log_id=request.args['log_id']
	# route_id=request.args['route_id']

	q="UPDATE `collection_request` SET `request_status`='Collect' WHERE `request_id`='%s'"%(rqid)
	update(q)

	data['status']="Success"
	data['method']="agent_confirm_request"
	return demjson.encode(data)

	

@api.route('/user_view_collection_report/',methods=['get','post'])
def user_view_collection_report():
	data={}
	# rqid=request.args['rqid']
	# company_id=request.args['company_id']
	# stock_id=request.args['stock_id']
	log_id=request.args['logid']
	# route_id=request.args['route_id']

	q="SELECT * FROM `collection_request` WHERE `user_id`=(SELECT `user_id` FROM `users` WHERE `log_id`='%s')"%(log_id)
	res=select(q)
	# q="UPDATE `collection_request` SET `request_status`='Collected' WHERE `request_id`='%s'"%(rqid)
	# update(q)
	data['data']=res
	data['status']="Success"
	data['method']="user_view_collection_report"
	return demjson.encode(data)

	
@api.route('/user_confirm_collect/',methods=['get','post'])
def user_confirm_collect():
	data={}
	rqid=request.args['rqid']
	# company_id=request.args['company_id']
	# stock_id=request.args['stock_id']
	# log_id=request.args['logid']
	# route_id=request.args['route_id']

	# q="SELECT * FROM `collection_request` WHERE `user_id`=(SELECT `user_id` FROM `users` WHERE `log_id`='%s')"%(log_id)
	# res=select(q)
	q="UPDATE `collection_request` SET `request_status`='Collected' WHERE `request_id`='%s'"%(rqid)
	update(q)
	# data['data']=res
	data['status']="Success"
	data['method']="user_confirm_collect"
	return demjson.encode(data)

	

@api.route('/user_view_tot_amount/',methods=['get','post'])
def user_view_tot_amount():
	data={}
	log_id=request.args['log_id']
	t_price=request.args['t_price']
	
	# q="UPDATE `collection_request` SET `request_status`='Collected' WHERE `request_id`='%s'"%(rqid)
	# update(q)
	q="SELECT * FROM `purchase_master` WHERE `user_id`=(SELECT `user_id` FROM `users` WHERE `log_id`='%s')"(log_id)
	res=select(q)
	data['data']=res
	data['status']="Success"
	data['method']="user_view_tot_amount"
	return demjson.encode(data)

	

@api.route('/user_payment/',methods=['get','post'])
def user_payment():
	data={}
	log_id=request.args['log_id']
	t_price=request.args['t_price']
	
	# q="UPDATE `collection_request` SET `request_status`='Collected' WHERE `request_id`='%s'"%(rqid)
	# update(q)
	q="INSERT INTO `payment` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `log_id`='%s'),'%s',CURDATE())"%(log_id,t_price)
	insert(q)
	# data['data']=res
	data['status']="Success"
	data['method']="user_payment"
	return demjson.encode(data)



@api.route('User_view_my_orders/',methods=['get','post'])
def User_view_my_orders():
	data={}
	log_id=request.args['logid']
	
	# q="UPDATE `collection_request` SET `request_status`='Collected' WHERE `request_id`='%s'"%(rqid)
	# update(q)
	q="SELECT *,`purchase_details`.`quantity` AS `quantity` FROM `purchase_details` INNER JOIN `purchase_master` USING(`pm_id`) INNER JOIN `stock` USING(`stock_id`) INNER JOIN `products` USING(`pro_id`) INNER JOIN `company` ON `company`.`com_id`=`stock`.`com_id` WHERE `user_id`=(SELECT `user_id` FROM `users` WHERE `log_id`='%s') ORDER BY `purchase_master`.`date` DESC"%(log_id)
	print(q)
	res=select(q)
	data['data']=res
	data['status']="Success"
	data['method']="User_view_my_orders"
	return demjson.encode(data)

@api.route('Waste_payment/',methods=['get','post'])
def Waste_payment():
	data={}
	log_id=request.args['log_id']
	wamount=request.args['wamount']
	
	# q="UPDATE `collection_request` SET `request_status`='Collected' WHERE `request_id`='%s'"%(rqid)
	# update(q)
	q="INSERT INTO `payment` VALUES(NULL,(SELECT `user_id` FROM `users` WHERE `log_id`='%s'),'%s',CURDATE())"%(log_id,wamount)
	print(q)
	insert(q)
	data['status']="Success"
	data['method']="Waste_payment"
	return demjson.encode(data)



@api.route('/agent_delivery_product/',methods=['get','post'])
def agent_delivery_product():
	data={}
	log_id=request.args['logid']
	
	q="SELECT *,CONCAT(`users`.`fname`,' ',`users`.`lname`) AS user_name,`users`.`phone` AS user_phone,`users`.`email` AS user_email,`purchase_master`.`total_amount` AS pt_amount FROM `purchase_master` INNER JOIN `purchase_details` USING(`pm_id`) INNER JOIN `users` USING(`user_id`) INNER JOIN `agents` ON `agent_id`=`assigned_agent` INNER JOIN `stock` USING(`com_id`) INNER JOIN `products` USING(`pro_id`) INNER JOIN `company` USING(`com_id`) WHERE `agent_id`=(SELECT `agent_id` FROM `agents` WHERE `log_id`='%s') GROUP BY `pm_id`"%(log_id)
	print(q)
	res=select(q)
	data['data']=res
	data['status']="Success"
	data['method']="agent_delivery_product"
	return demjson.encode(data)



@api.route('/agent_confirm_delivery/',methods=['get','post'])
def agent_confirm_delivery():
	data={}
	pm_ids=request.args['pm_ids']
	
	q="UPDATE `purchase_master` SET `delivery_status`='Successfully Delivered' WHERE `pm_id`='%s'"%(pm_ids)
	update(q)
	# data['data']=res
	data['status']="Success"
	data['method']="agent_confirm_delivery"
	return demjson.encode(data)
