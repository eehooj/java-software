package me.torissi.chapter3;

public class HtmlExporter implements Exporter {

    @Override
    public String export(SummaryStatistics summaryStatistics) {
        String result = "<!DOCTYPE html>";
        result += "<html lang='en'>";
        result += "<head>";
        result += "    <meta charset='UTF-8'>";
        result += "    <title>Bank Transaction Report</title>";
        result += "</head>";
        result += "<body>";
        result += "    <ul>";
        result += "        <li><strong>The sum is</strong>:" + summaryStatistics.getSum() + "</li>";
        result += "        <li><strong>The max is</strong>:" + summaryStatistics.getMax() + "</li>";
        result += "        <li><strong>The average is</strong>:" + summaryStatistics.getAverage() + "</li>";
        result += "        <li><strong>The min is</strong>:" + summaryStatistics.getMin() + "</li>";
        result += "    </ul>";
        result += "</body>";
        result += "</html>";

        return result;
    }
}
