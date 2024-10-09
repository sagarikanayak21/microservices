package com.sn.currency.conversion.controller;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.sn.currency.conversion.proxy.CurrencyExchangeProxy;
import com.sn.currency.conversion.vo.CurrencyConversion;

@RestController
public class CurrencyConversionController {
	@Autowired
	CurrencyExchangeProxy currencyExchangeProxy;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);

		ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);
		CurrencyConversion cc = responseEntity.getBody();
		return new CurrencyConversion(cc.getId(), cc.getFrom(), cc.getTo(), cc.getConversionMultiple(), quantity,
				quantity.multiply(cc.getConversionMultiple()), cc.getEnvironment());
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {
		HashMap<String, String> uriVariables = new HashMap<>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);


		CurrencyConversion cc = currencyExchangeProxy.retrieveExchangeValue(from, to);
		return new CurrencyConversion(cc.getId(), cc.getFrom(), cc.getTo(), cc.getConversionMultiple(), quantity,
				quantity.multiply(cc.getConversionMultiple()), cc.getEnvironment());
	}

}
