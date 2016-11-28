//存放主要交互逻辑js代码
//javascript模块化
var seckill={
		//封装秒杀相关的URL

		URL : {
			now : function(){
				return  '/seckill/seckill/time/now';
			},
            exposer:function(seckillId){
            	return  '/seckill/seckill/'+seckillId+'/exposer';
            },
			execution:function(seckillId,md5){
				return "/seckill/seckill/"+seckillId+"/"+md5+"/execution";
			}
		},
		handlerSeckillKill:function(seckillId,node){
			//处理秒杀逻辑
			node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">执行秒杀</button>');
			$.post(seckill.URL.exposer(seckillId),{},function(result){
					if(result && result['success']){
						var exposer=result['data'];
						if(exposer['exposed']){
							//开启秒杀
							//获取秒杀地址
							var md5=exposer['md5'];
							var killUrl=seckill.URL.execution(seckillId,md5);
							console.log('killUrl='+killUrl);
							
							//绑定一次点击事件
							
							$('#killBtn').one('click',function(){
								//禁用按钮
								$(this).addClass('disabled');
								//发送秒杀请求
								$.post(killUrl,{},function(result){
									if(result && result['success']){
										var killResult=result['data'];
										var state=killResult['killResult'];
										var stateInfo=killResult['stateInfo'];
										//显示秒杀结果
										node.html('<span class="label label-success">'+stateInfo+'</span>');
									}else{
										console.log('result='+result);
									}
								});
								
							});
							
							node.show();
						}else{
							//未开启秒杀
							var start=exposer['start'];
							var end=exposer['end'];
							var now=exposer['now'];
							//重新计算计时逻辑，防止不同机器的计时有偏差
							seckill.countDown(seckillId,now,start,end);
							
						}
					}else{
						console.log('result='+result);
					}
			});
			
		},
//      验证手机号
		validatePhone: function(phone){
			if(phone != null && phone.length == 11 && !isNaN(phone)){
				return true;
			}else{
				return false;
			}
		},
		
		countDown:function(seckillId,nowTime,startTime,endTime){
			var seckillBox=$('#seckill-box');
			if(nowTime > endTime){
				//秒杀结束
				seckillBox.html("秒杀结束！");	
			}
			if(nowTime < startTime){
				//秒杀未开始
				var killTime=new Date(startTime + 1000);
				seckillBox.countdown(killTime,function(event){
					//时间格式
					var format=event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
					seckillBox.html(format);
					//计时完成后回调事件
				}).on('finish.countdown',function(){
					//获取秒杀地址，控制实现逻辑，执行秒杀
					seckill.handlerSeckillKill(seckillId,seckillBox);
					
				});
			}else{
				//秒杀开始
				seckill.handlerSeckillKill(seckillId,seckillBox);
			}
		},
//		详情页秒杀逻辑
		detail:{
//			详情页初始化
			init:function(params){
//				手机验证和登录，计时交互
//				规划交互流程
//				在cookie中查找手机号
				var killPhone=$.cookie('userPhone');
				var startTime=params['startTime'];
				var endTime=params['endTime'];
				var seckillId=params['seckillId'];
//              验证手机号
				if(!seckill.validatePhone(killPhone)){
					//绑定手机号
					//控制输出
					var killPhoneModal=$('#killPhoneModal');
					killPhoneModal.modal({
						
						show:true,//显示弹出层
						backdrop: 'static',//禁止位置关闭
						keyboard: 'false'//关闭键盘事件
					});
					
					$('#killPhoneBtn').click(function(){
						
						var inputPhone=$('#killPhoneKey').val();
						if(seckill.validatePhone(inputPhone)){
							//电话写入cookie
							$.cookie('userPhone',inputPhone,{expires:7,path:'/seckill'})
							//刷新页面
							window.location.reload();
						}else{
							$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(500);
						}
					});
				}
				//已经登录
				//ajax计时交互
				
				$.get(seckill.URL.now(),{},function(result){
					if(result != null && result['success']){
						var timeNow=result['data'];
						//时间判断
						seckill.countDown(seckillId,timeNow,startTime,endTime);
					}
					
					
				});
				
			}
			
		}
		
		
}

