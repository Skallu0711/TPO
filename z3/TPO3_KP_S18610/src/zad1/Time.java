/**
 *
 *  @author Kamiński Patryk S18610
 *
 */

package zad1;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Time {

    private static Map<DayOfWeek, String> dniTyg = Stream.of(new Object[][]{
            {DayOfWeek.SUNDAY, "(niedziela)"},
            {DayOfWeek.MONDAY, "(poniedziałek)"},
            {DayOfWeek.TUESDAY, "(wtorek)"},
            {DayOfWeek.WEDNESDAY, "(środa)"},
            {DayOfWeek.THURSDAY, "(czwartek)"},
            {DayOfWeek.FRIDAY, "(piątek)"},
            {DayOfWeek.SATURDAY, "(sobota)"},
    }).collect(Collectors.toMap(data -> (DayOfWeek) data[0], data -> (String) data[1]));

    private static Map<Month, String> msc = Stream.of(new Object[][]{
            {Month.JANUARY, "stycznia"},
            {Month.FEBRUARY, "lutego"},
            {Month.MARCH, "marca"},
            {Month.APRIL, "kwietnia"},
            {Month.MAY, "maja"},
            {Month.JUNE, "czerwca"},
            {Month.JULY, "lipca"},
            {Month.AUGUST, "sierpnia"},
            {Month.SEPTEMBER, "września"},
            {Month.OCTOBER, "października"},
            {Month.NOVEMBER, "listopada"},
            {Month.DECEMBER, "grudnia"}
    }).collect(Collectors.toMap(data -> (Month) data[0], data -> (String) data[1]));

    public static String passed(String from, String to) {
        Locale.setDefault(Locale.ENGLISH);

        try {
            if (from.contains("T") && to.contains("T")) {
                return info(LocalDateTime.parse(from), LocalDateTime.parse(to));
            }
            return info(LocalDate.parse(from), LocalDate.parse(to));

        } catch (DateTimeParseException ex) {
            return "*** java.time.format.DateTimeParseException: " + ex.getMessage();
        }
    }

    private static String info(LocalDate from, LocalDate to) {
        long dniPomiedzy = ChronoUnit.DAYS.between(from, to);
        double tygodniePomiedzy = dniPomiedzy / 7.0;

        return "Od " + from.getDayOfMonth() + " " + msc.get(from.getMonth()) + " " + from.getYear() + " " + dniTyg.get(from.getDayOfWeek()) +
                " do " + to.getDayOfMonth() + " " + msc.get(to.getMonth()) + " " + to.getYear() + " " + dniTyg.get(to.getDayOfWeek()) + "\n- mija: " + dniPomiedzy +
                (dniPomiedzy == 1 ? " dzień, " : " dni, ") + "tygodni " + (dniPomiedzy % 7.0 == 0 ? String.valueOf(tygodniePomiedzy).split("\\.")[0] : String.format("%.2f", tygodniePomiedzy)) +
                getKalendarz(from, to);
    }

    private static String info(LocalDateTime from, LocalDateTime to) {
        ZonedDateTime zFrom = ZonedDateTime.of(from, ZoneId.of("Europe/Warsaw"));
        ZonedDateTime zTo = ZonedDateTime.of(to, ZoneId.of("Europe/Warsaw"));

        long dniPomiedzy = ChronoUnit.DAYS.between(zFrom.toLocalDate(), zTo.toLocalDate());
        double tygodniePomiedzy = dniPomiedzy / 7.0;

        long godz = Duration.between(zFrom, zTo).toHours();
        long min = Duration.between(zFrom, zTo).toMinutes();

        if (dniPomiedzy % 7.0 == 0) {

            return "Od " + zFrom.getDayOfMonth() + " " + msc.get(zFrom.getMonth()) + " " + zFrom.getYear() + " " + dniTyg.get(zFrom.getDayOfWeek()) +
                    " godz. " + zFrom.format(DateTimeFormatter.ofPattern("hh:mm")) +
                    " do " + zTo.getDayOfMonth() + " " + msc.get(zTo.getMonth()) + " " + zTo.getYear() + " " + dniTyg.get(zTo.getDayOfWeek()) +
                    " godz. " + zTo.format(DateTimeFormatter.ofPattern("hh:mm")) + "\n- mija: " + dniPomiedzy +
                    (dniPomiedzy == 1 ? " dzień, " : " dni, ") + "tygodni " + String.valueOf(tygodniePomiedzy).split("\\.")[0] +
                    "\n- godzin: " + godz + ", minut: " + min + getKalendarz(zFrom.toLocalDate(), zTo.toLocalDate());
        }
        if (dniPomiedzy == 1) {

            return "Od " + zFrom.getDayOfMonth() + " " + msc.get(zFrom.getMonth()) + " " + zFrom.getYear() + " " + dniTyg.get(zFrom.getDayOfWeek()) +
                    " godz. " + zFrom.format(DateTimeFormatter.ofPattern("hh:mm")) +
                    " do " + zTo.getDayOfMonth() + " " + msc.get(zTo.getMonth()) + " " + zTo.getYear() + " " + dniTyg.get(zTo.getDayOfWeek()) +
                    " godz. " + zTo.format(DateTimeFormatter.ofPattern("hh:mm")) + "\n- mija: " + dniPomiedzy +
                    " dzień, " + "tygodni " + String.format("%.2f", tygodniePomiedzy) +
                    "\n- godzin: " + godz + ", minut: " + min + getKalendarz(zFrom.toLocalDate(), zTo.toLocalDate());
        }

        return "Od " + zFrom.getDayOfMonth() + " " + msc.get(zFrom.getMonth()) + " " + zFrom.getYear() + " " + dniTyg.get(zFrom.getDayOfWeek()) +
                " godz. " + zFrom.format(DateTimeFormatter.ofPattern("hh:mm")) +
                " do " + zTo.getDayOfMonth() + " " + msc.get(zTo.getMonth()) + " " + zTo.getYear() + " " + dniTyg.get(zTo.getDayOfWeek()) +
                " godz. " + zTo.format(DateTimeFormatter.ofPattern("hh:mm")) + "\n- mija: " + dniPomiedzy +
                " dni, " + "tygodni " + String.format("%.2f", tygodniePomiedzy) +
                "\n- godzin: " + godz + ", minut: " + min + getKalendarz(zFrom.toLocalDate(), zTo.toLocalDate());
    }

    private static String getKalendarz(LocalDate from, LocalDate to) {

        long dniPomiedzy = ChronoUnit.DAYS.between(from, to);

        String kalendarzowo = "";

        if (dniPomiedzy != 0) {
            int r = Period.between(from, to).getYears();
            int m = Period.between(from, to).getMonths();
            int d = Period.between(from, to).getDays();

            kalendarzowo = "\n- kalendarzowo: ";

            if (r != 0) {
                if (r == 1) {
                    kalendarzowo += r + " rok, ";
                } else if (r < 5) {
                    kalendarzowo += r + " lata, ";
                } else {
                    kalendarzowo += r + " lat, ";
                }
            }
            if (m != 0) {
                if (m == 1) {
                    kalendarzowo += m + " miesiąc, ";
                } else if (m < 5) {
                    kalendarzowo += m + " miesiące, ";
                } else {
                    kalendarzowo += m + " miesięcy, ";
                }
            }
            if (d != 0) {
                if (d == 1) {
                    kalendarzowo += d + " dzień, ";
                } else {
                    kalendarzowo += d + " dni, ";
                }
            }
            kalendarzowo = kalendarzowo.substring(0, kalendarzowo.length() - 2);
        }
        return kalendarzowo;
    }
}
