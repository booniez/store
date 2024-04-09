package com.storehub.store.flow.utils;

import com.storehub.store.common.core.util.SpringContextHolder;
import org.flowable.common.engine.impl.cfg.IdGenerator;

public class IdWorkerIdGenerator implements IdGenerator {
	@Override
	public String getNextId() {
		return SpringContextHolder.getBean(IdWorker.class).nextId()+"";
	}
}
