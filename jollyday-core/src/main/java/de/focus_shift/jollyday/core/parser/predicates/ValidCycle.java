package de.focus_shift.jollyday.core.parser.predicates;

import de.focus_shift.jollyday.core.spi.Limited;

import java.util.function.Predicate;

public class ValidCycle implements Predicate<Limited> {

  private final int year;

  public ValidCycle(final int year) {
    this.year = year;
  }

  @Override
  public boolean test(Limited limited) {
    switch (limited.cycle()) {
      case EVERY_YEAR:
        return true;
      case ODD_YEARS:
        return year % 2 != 0;
      case EVEN_YEARS:
        return year % 2 == 0;
      case TWO_YEARS:
        return isValidWithReferenceYear(limited, 2);
      case THREE_YEARS:
        return isValidWithReferenceYear(limited, 3);
      case FOUR_YEARS:
        return isValidWithReferenceYear(limited, 4);
      case FIVE_YEARS:
        return isValidWithReferenceYear(limited, 5);
      case SIX_YEARS:
        return isValidWithReferenceYear(limited, 6);
      default:
        throw new IllegalArgumentException("Cannot handle unknown cycle type '" + limited.cycle() + "'.");
    }
  }

  /**
   * Will validate if a given year based on the reference year (validFrom/validTo) is valid based on the cycle strategy.
   * <p>
   * Note: no need to test whether we are in range, as this is already done in {@link ValidFromTo}
   *
   * @param limited    provides the reference years. First we use validFrom and if not given validTo
   * @param cycleYears number of years to start the cycle starting from validFrom/validTo
   * @return true if the given year based on validFrom/validTo and the cycle is valid, otherwise false
   */
  private boolean isValidWithReferenceYear(Limited limited, int cycleYears) {
    if (limited.validFrom() != null) {
      return (year - limited.validFrom().getValue()) % cycleYears == 0;
    } else if (limited.validTo() != null) {
      return (limited.validTo().getValue() - year) % cycleYears == 0;
    }

    throw new IllegalArgumentException("Cannot handle cycle type '" + limited.cycle() + "' without any reference year.");
  }
}
