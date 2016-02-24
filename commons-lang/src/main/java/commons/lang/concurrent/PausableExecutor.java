/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2005 All Rights Reserved.
 */
package commons.lang.concurrent;

import java.util.concurrent.Executor;

/**
 * @author calvin.lil@alibaba-inc.com
 *
 * @version $Id$
 */
public interface PausableExecutor extends Executor {

    /**
     * 暂停工作
     */
    public void pause();

    /**
     * 恢复工作
     */
    public void resume();

    /**
     * 目前是否处于暂停状态
     * 
     * @return
     */
    public boolean isPaused();
}
