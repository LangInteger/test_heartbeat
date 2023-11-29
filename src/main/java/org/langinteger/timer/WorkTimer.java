package org.langinteger.timer;

import java.util.TimerTask;
import java.util.function.Consumer;
import lombok.Data;

@Data
public class WorkTimer extends TimerTask{
		private String id;
		private int interval;
		private Consumer<String> timeoutCallback;
		
		public WorkTimer( String sid, int intv, Consumer<String> timeoutCallback) {
			id = sid;
			interval = intv;
			this.timeoutCallback = timeoutCallback;
		}
		
		public String getID() {
			return id;
		}
		
		public int getInterval() {
			return interval;
		}

		@Override
		public void run() {
			timeoutCallback.accept(id);
		}
	
}    
