// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distimeRangesAvailibuted under the License is distimeRangesAvailibuted on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Queue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;

/* A meeting request has:
   - a name
   - a duration in minutes
   - a collection of attendees
   
   For a particular time slot to work, all attendees must be free to attend the meeting. When a query is made, it will be given a collection of all known events. Each event has:
   - a name
   - a time range
   - a collection of attendees
   A time range will give you the start time, the end time, and the duration of the event. If you want to know more, open the TimeRange.java file.
*/

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Creates an ArrayList to hold busy times
    List<TimeRange> busyTimes = new ArrayList<>();

    // Gets info of request
    Collection<String> attendees = request.getAttendees();
    long dur = request.getDuration();

    // Special case where there can be no meetings of that duration 
    if(dur > TimeRange.END_OF_DAY - TimeRange.START_OF_DAY || dur < 0){
      return busyTimes; 
    }
    
    // looks at every event
    for(Event event : events){ 
      Collection<String> eventAtt = event.getAttendees();
      // looks at all the people attending the event
      for(String attendee : eventAtt){ 
        if(attendees.contains(attendee)){
          busyTimes.add(event.getWhen());
          break; // found someone in this event, we can stop searching this one
        }
      }
    }
    List<TimeRange> timeRangesAvail = new ArrayList(); 

    // Assume whole day is free at first
    timeRangesAvail.add(TimeRange.WHOLE_DAY);

    // Sort the busy times by start date for easier parsing
    Collections.sort(busyTimes, TimeRange.ORDER_BY_START); 

    // Goes through each busyTime
    for(TimeRange busyTime : busyTimes){
      int origLen = timeRangesAvail.size();
      for(int index = 0; index < origLen; index ++){
        TimeRange freeTime = timeRangesAvail.get(index);
        // remove fully, split free by busyTime
        if(timeRangesAvail.get(index).contains(busyTime)){ 
          TimeRange secHalf = TimeRange.fromStartDuration(busyTime.end(), freeTime.end() - busyTime.end());
          TimeRange firstHalf = TimeRange.fromStartDuration(freeTime.start(), freeTime.duration() - secHalf.duration() - busyTime.duration());
          timeRangesAvail.remove(freeTime); // remove old time
          index--; // back the counter up because we removed an item
          origLen --; // we need to view one less item
          // only adds to list if the free time >= duration wanted
          if(firstHalf.duration() >= dur) 
            timeRangesAvail.add(firstHalf);
          if(secHalf.duration() >= dur)
            timeRangesAvail.add(secHalf);
        } else if(freeTime.overlaps(busyTime)){ //starts are the same, but ends arent, cut free by some amount
          int newStart = 0;
          int newEnd = 0;
          //There are two cases, if the free time occurs before or after the busy time
          if(freeTime.start() < busyTime.start()){
            newStart = freeTime.start();
            newEnd = freeTime.duration() - (freeTime.end() - busyTime.start());
          } else {
            newStart = busyTime.end();
            newEnd = freeTime.duration() - (busyTime.end() - freeTime.start());
          }
          TimeRange cutTime = TimeRange.fromStartDuration(newStart, newEnd);
          timeRangesAvail.remove(freeTime);
          index--; //back the counter up because we removed an item
          origLen --; //we need to view one less item
          if(cutTime.duration() >= dur)
            timeRangesAvail.add(cutTime);
        }
      }
    }
    Collections.sort(timeRangesAvail, TimeRange.ORDER_BY_START); //Order by start times
    return timeRangesAvail; 
  }
}
