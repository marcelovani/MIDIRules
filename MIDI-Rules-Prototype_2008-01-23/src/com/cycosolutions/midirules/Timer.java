//	MIDI Rules | A Rule-Based MIDI Processing System
//	http://www.sourceforge.net/projects/midi-rules/
//
//	Copyright (C) 2006  Christoph Gerkens 
//	chgerkens@users.sourceforge.net
//	
//	This program is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	This program is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License along
//	with this program; if not, write to the Free Software Foundation, Inc.,
//	51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

package com.cycosolutions.midirules;

public abstract class Timer extends Thread {
	  
  private long expirationTime;
  private boolean scheduled = false;
  private boolean expired = false;
  
  /**
   * @param executionTime The executionTime to set.
   */
  public void setExpirationTime(long expirationTime) {
    synchronized(this) {
      this.expirationTime = expirationTime;
      this.scheduled = true;
      setExpired(false);
      this.notify();
    }
  }

  public long getExpirationTime() {
    return expirationTime;
  }
  
	public boolean isExpired() {
		return expired;
	}
	
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
  
  public void run() {
    while(!interrupted()) {
      synchronized(this) {
        if(!scheduled) {
          try {
            this.wait();
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        }
      }
      while(System.currentTimeMillis() < getExpirationTime()) {
        try {
        	final long timeout = getExpirationTime() - System.currentTimeMillis();
          sleep(timeout < 0 ? 0 : timeout);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      synchronized(this) {  
    	  scheduled = false;
    	  setExpired(true);
      }
      timerAction();
    }
  }
  
  /**
   * Called when the timer expires.
   */
  protected abstract void timerAction();
}
