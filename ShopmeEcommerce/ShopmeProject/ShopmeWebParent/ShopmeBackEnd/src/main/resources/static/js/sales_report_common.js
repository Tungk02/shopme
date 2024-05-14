// Sales Report Common
var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000;


function setupButtonEventHandlers(reportType, callbackFunction) {
	startDateField = document.getElementById('startDate' + reportType);
	endDateField = document.getElementById('endDate' + reportType);

	$(".button-sales-by" + reportType).on("click", function() {
		$(".button-sales-by" + reportType).each(function(e) {
			$(this).removeClass('btn-primary').addClass('btn-light');
		});

		$(this).removeClass('btn-light').addClass('btn-primary');

		period = $(this).attr("period");
		if (period) {
			callbackFunction(period);
			$("#divCustomDateRange" + reportType).addClass("d-none");
		} else {
			$("#divCustomDateRange" + reportType).removeClass("d-none");
		}

	});

	initCustomDateRange(reportType);

	$("#buttonViewReportByDateRange" + reportType).on("click", function(e) {
		validateDateRange(reportType, callbackFunction);
	});
}

function validateDateRange(reportType, callbackFunction) {
	startDateField = document.getElementById('startDate' + reportType);
	days = calculateDays(reportType);

	startDateField.setCustomValidity("");

	if (days >= 7 && days <= 30) {
		callbackFunction("custom");
	} else {
		startDateField.setCustomValidity("Dates must be in the range of 7..30 days");
		startDateField.reportValidity();
	}
}

function calculateDays(reportType) {
	startDateField = document.getElementById('startDate' + reportType);
	endDateField = document.getElementById('endDate' + reportType);

	startDate = startDateField.valueAsDate;
	endDate = endDateField.valueAsDate;

	differenceInMilliseconds = endDate - startDate;
	return differenceInMilliseconds / MILLISECONDS_A_DAY;
}

function initCustomDateRange(reportType) {
	startDateField = document.getElementById('startDate' + reportType);
	endDateField = document.getElementById('endDate' + reportType);

	toDate = new Date();
	endDateField.valueAsDate = toDate;

	fromDate = new Date();
	fromDate.setDate(toDate.getDate() - 30);
	startDateField.valueAsDate = fromDate;
}

function formatcurrency(amount) {
	formattedAmount = $.number(amount, decimalDigits, decimalPointType, thousandsPointType);
	return prefixCurrencySymbol + formattedAmount + suffixCurrencySymbol;
}

function getChartTitle(period) {
	if (period == "last_7_days") return "Sales Last in 7 Days";
	if (period == "last_28_days") return "Sales Last in 28 Days";
	if (period == "last_6_months") return "Sales Last in 6 Months";
	if (period == "last_year") return "Sales Last Year";
	if (period == "last_3_year") return "Sales Last 3 Year";
	if (period == "custom") return "Custom Date Range";

	return "";
}

function getDenomirator(period, reportType) {
	if (period == "last_7_days") return 7;
	if (period == "last_28_days") return 28;
	if (period == "last_6_months") return 6;
	if (period == "last_year") return 12;
	if (period == "last_3_year") return 3 * 12;
	if (period == "custom") return calculateDays(reportType);

	return 7;
}

function setSalesAmount(period, reportType, labelTotalItems) {
	$("#textTotalGrossSales" + reportType).text(formatcurrency(totalGrossSales));
	$("#textTotalNetSales" + reportType).text(formatcurrency(totalNetSales));

	denomirator = getDenomirator(period, reportType);

	$("#textAvgGrossSales" + reportType).text(formatcurrency(totalGrossSales / denomirator));
	$("#textAvgNetSales" + reportType).text(formatcurrency(totalGrossSales / denomirator));
	$("#labelTotalItems" + reportType).text(labelTotalItems);
	$("#textTotalItems" + reportType).text((totalItems));
}

function formatChartData(data, columnIndex1, columnIndex2) {
	var formatter = new google.visualization.NumberFormat({
		prefix: prefixCurrencySymbol,
		suffix: suffixCurrencySymbol,
		decimalSymbol: decimalPointType,
		groupingSymbol: thousandsPointType,
		fractionDigits: decimalDigits
	});

	formatter.format(data, columnIndex1);
	formatter.format(data, columnIndex2);
}
