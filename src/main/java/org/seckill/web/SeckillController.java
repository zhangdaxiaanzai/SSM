package org.seckill.web;

import java.util.List;

import org.seckill.entity.Seckill;
import org.seckill.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/listSeckill",method=RequestMethod.GET)
	public String listSeckill(Model model){
		//获取列表页
		List<Seckill> listSeckill=seckillService.getSeckillList();
		model.addAttribute("listSeckill", listSeckill);
		
		return "list";
	}
	
	
	public String detail(Model model,@PathVariable Long seckillId){
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
}
