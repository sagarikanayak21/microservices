package com.sn.currency.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.sn.currency.exchange.repository.CurrencyExchangeRepository;
import com.sn.currency.exchange.vo.CurrencyExchange;

@RestController
public class CurrencyExchangeController {
	@Autowired
	CurrencyExchangeRepository  currencyExchangeRepository;
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
		CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
		
		if(null==currencyExchange) {
			throw new RuntimeException("unable to find data from " + from + " to " + to);
		}
		
		String port = environment.getProperty("local.server.port");
		String profiles[] = environment.getActiveProfiles();
		currencyExchange.setEnvironment(profiles[0]);
		return currencyExchange;
	}

}
