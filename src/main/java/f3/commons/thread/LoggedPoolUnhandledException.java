/*
 * Copyright (c) 2010-2017 fork3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package f3.commons.thread;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author n3k0nation
 *
 */
@Slf4j
public class LoggedPoolUnhandledException implements BiConsumer<Runnable, Throwable> {
	public LoggedPoolUnhandledException() {
	}

	@Override
	public void accept(Runnable t, Throwable u) {
		if(u != null) {
			log.error("", u);
		} else if(t instanceof Future<?>) {
			final Future<?> f = (Future<?>) t;
			try {
				f.get();
			} catch(CancellationException e) { 
				//ignore
			} catch(InterruptedException e) { 
				Thread.currentThread().interrupt();
			} catch(ExecutionException e) {
				log.error("", e);
			}
		}
	}

}
