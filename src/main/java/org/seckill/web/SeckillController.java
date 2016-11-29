package org.seckill.web;

import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillService seckillService;
	
	
	
	/**
	 * 获取秒杀列
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/listSeckill",method=RequestMethod.GET)
	public String listSeckill(Model model){
		//获取列表页
		List<Seckill> listSeckill=seckillService.getSeckillList();
		model.addAttribute("listSeckill", listSeckill);
		
		return "list";
	}
	
	/**
	 * 获取秒杀详情
	 * @param model
	 * @param seckillId
	 * @return
	 */
	@RequestMapping(value="/{seckillId}/detailSeckill",method=RequestMethod.GET)
	public String detail(Model model,@PathVariable("seckillId") Long seckillId){
		if(seckillId == null){
			return "redirect:/seckill/listSeckill";
		}
		Seckill scekill=seckillService.getSeckillById(seckillId);
		if(scekill==null){
			return "forward:/seckill/listSeckill";
		}
		model.addAttribute("scekill",scekill);
		return "detail";
	}
	
	/**
	 * 秒杀前是否暴露秒杀地址等
	 * @param seckillId
	 * @return
	 */
	//ajax json produces:解决数据乱码
	@RequestMapping(value="/{seckillId}/exposer",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer( @PathVariable("seckillId") Long seckillId){
		SeckillResult<Exposer> seckillResult;
		
		try {
			Exposer exposer=seckillService.exportSeckillUrl(seckillId);
			seckillResult=new SeckillResult<Exposer>(true, exposer);
			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return new SeckillResult<Exposer>(false, e.getMessage());
		}
		 
		
		
		return seckillResult;
		
	}
	/**
	 * 执行秒杀
	 * @param seckillId
	 * @param md5
	 * @param userPhone
	 * @return
	 */
	@RequestMapping(value="/{seckillId}/{md5}/execution",method=RequestMethod.POST,produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
			@PathVariable("md5") String md5,
			@CookieValue(value="userPhone",required=false) Long userPhone){
		
		SeckillResult<SeckillExecution> result;
		
		if(userPhone == null){
			return new SeckillResult<SeckillExecution>(false, "未注册");
		}
		try {
			SeckillExecution execution=seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
			return new SeckillResult<SeckillExecution>(true, execution); 
		} catch (RepeatKillException e) {
			SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		}catch (SeckillCloseException e) {
			SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.END);
			return new SeckillResult<SeckillExecution>(true, execution);
		}catch(Exception e) {
			// TODO: handle exception
			logger.error(e.getMessage(), e);
			SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.INNER_KILL);
			return new SeckillResult<SeckillExecution>(true, execution);
		}
	}
	
	/**
	 * 返回系统时间
	 * @return
	 */
	@RequestMapping(value="/time/now",method=RequestMethod.GET)
	@ResponseBody
	public SeckillResult<Long> time(){
		Date now=new Date();
		return new SeckillResult<Long>(true, now.getTime());
		
	}
	
}
