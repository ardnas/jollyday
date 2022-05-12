package de.focus_shift.parser.impl;

import de.focus_shift.Holiday;
import de.focus_shift.parser.functions.CreateHoliday;
import de.focus_shift.parser.functions.FindWeekDayInMonth;
import de.focus_shift.parser.predicates.ValidLimitation;
import de.focus_shift.spi.Relation;
import de.focus_shift.spi.RelativeToWeekdayInMonth;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * <p>
 * RelativeToWeekdayInMonthParser class.
 * </p>
 *
 * @author Sven
 * @version $Id: $
 */
public class RelativeToWeekdayInMonthParser implements Function<Integer, List<Holiday>> {

  private final List<RelativeToWeekdayInMonth> relativeToWeekdayInMonths;

  public RelativeToWeekdayInMonthParser(List<RelativeToWeekdayInMonth> relativeToWeekdayInMonths) {
    this.relativeToWeekdayInMonths = relativeToWeekdayInMonths;
  }

  @Override
  public List<Holiday> apply(Integer year) {
    return relativeToWeekdayInMonths.stream()
      .filter(new ValidLimitation(year))
      .map(rwm -> {
        LocalDate date = new FindWeekDayInMonth(year).apply(rwm.weekdayInMonth()).plusDays(1);
        int direction = (rwm.when() == Relation.BEFORE ? -1 : 1);
        while (date.getDayOfWeek() != rwm.weekday()) {
          date = date.plusDays(direction);
        }
        return new CreateHoliday(date).apply(rwm);
      })
      .collect(toList());
  }
}