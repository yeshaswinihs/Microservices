package com.apigateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class MyPostFilter implements GlobalFilter, Ordered {

	final Logger logger = LoggerFactory.getLogger(MyPostFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		return chain.filter(exchange).then(Mono.fromRunnable(() -> {

			logger.info("Global Post filter.chain.chain.");

		}));
	}

	@Override
	public int getOrder() {
//	lower index for post filter will be executed at last, highest ordered filter will be executed at first
		return 0;
	}

}
