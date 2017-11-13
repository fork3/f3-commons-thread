/*
 * Copyright (c) 2010-2017 fork3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package f3.commons.thread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author n3k0nation
 *
 */
public class ThreadPool extends ThreadPoolExecutor {
	public static ThreadPool newThreadPool(int corePoolSize, ThreadFactory threadFactory) {
		return new ThreadPool(corePoolSize, threadFactory);
	}

	private final List<BiConsumer<Thread, Runnable>> beforeExecuteHook = new CopyOnWriteArrayList<>();
	private final List<BiConsumer<Runnable, Throwable>> afterExecuteHook = new CopyOnWriteArrayList<>();

	public ThreadPool(int corePoolSize) {
      super(corePoolSize, corePoolSize, 0l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
  }

	public ThreadPool(int corePoolSize, ThreadFactory threadFactory) {
      super(corePoolSize, corePoolSize, 0l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory);
  }

	public ThreadPool(int corePoolSize, RejectedExecutionHandler handler) {
      super(corePoolSize, corePoolSize, 0l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), handler);
  }

	public ThreadPool(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
      super(corePoolSize, corePoolSize, 0l, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), threadFactory, handler);
  }

	public void addBeforeHook(BiConsumer<Thread, Runnable> consumer) {
		beforeExecuteHook.add(consumer);
	}

	public void addAfterHook(BiConsumer<Runnable, Throwable> consumer) {
		afterExecuteHook.add(consumer);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		beforeExecuteHook.forEach(consumer -> consumer.accept(t, r));
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		afterExecuteHook.forEach(consumer -> consumer.accept(r, t));
	}

}
