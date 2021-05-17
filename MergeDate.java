package com.date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MergeDate {

  public static void main(String[] args) throws Exception {
    List<DateRange> dateRanges = new ArrayList<>();
    dateRanges.add(new DateRange(LocalDate.of(2019, 1, 1), LocalDate.of(2019, 1, 15)));
    dateRanges.add(new DateRange(LocalDate.of(2019, 1, 15), LocalDate.of(2019, 2, 15)));
    dateRanges.add(new DateRange(LocalDate.of(2019, 3, 10), LocalDate.of(2019, 4, 12)));
    dateRanges.add(new DateRange(LocalDate.of(2019, 4, 10), LocalDate.of(2019, 5, 15)));

    System.out.println("Input =");
    dateRanges.stream().forEach(dateRange -> System.out.println(dateRange.getStartDate() + " - " + dateRange.getEndDate()));

    List<DateRange> mergedDateRange = mergeDateRange(dateRanges);

    System.out.println("Output =");
    mergedDateRange.stream().forEach(dateRange -> System.out.println(dateRange.getStartDate() + " - " + dateRange.getEndDate()));
  }

  private static List<DateRange> mergeDateRange(List<DateRange> dateRanges) {
    Set<DateRange> mergedDateRangeSet = new HashSet<>();
    Collections.sort(dateRanges, DateRange.START_DATE_COMPARATOR);

    mergedDateRangeSet.add(dateRanges.get(0));
    for (int index = 1; index < dateRanges.size(); index++) {
      DateRange current = dateRanges.get(index);
      List<DateRange> toBeAdded = new ArrayList<>();
      Boolean rangeMerged = false;
      for (DateRange mergedRange : mergedDateRangeSet) {
        DateRange merged = checkOverlap(mergedRange, current);
        if (merged == null) {
          toBeAdded.add(current);
        }
        else {
          mergedRange.setEndDate(merged.getEndDate());
          mergedRange.setStartDate(merged.getStartDate());
          rangeMerged = true;
          break;
        }
      }
      if (!rangeMerged) {
        mergedDateRangeSet.addAll(toBeAdded);
      }
      toBeAdded.clear();
    }
    List<DateRange> mergedDateRangeList = new ArrayList<>(mergedDateRangeSet);
    Collections.sort(mergedDateRangeList, DateRange.START_DATE_COMPARATOR);
    return mergedDateRangeList;
  }

 
  private static DateRange checkOverlap(DateRange mergedRange, DateRange current) {
    if (mergedRange.getStartDate().isAfter(current.getEndDate()) || mergedRange.getEndDate().isBefore(current.getStartDate())) {
      return null;
    }
    else {
      return new DateRange(mergedRange.getStartDate().isBefore(current.getStartDate()) ? mergedRange.getStartDate() : current.getStartDate(),
        mergedRange.getEndDate().isAfter(current.getEndDate()) ? mergedRange.getEndDate() : current.getEndDate());
    }
  }
}
