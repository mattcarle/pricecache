<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<h2>PriceCache: ${viewDescription}</h2>
	<table border="1">
		<tr>
			<th>Ticker</th>
			<th>RIC</th>
			<th>SEDOL</th>
			<th>CUSIP</th>
			<th>ISIN</th>
			<th>BBGID</th>
			<th>Price</th>
			<th>Vendor</th>
			<th>Create Time</th>
		</tr>
		<c:forEach items="${prices}" var="price">
			<tr>
				<td><a href="<c:url value="/prices/latest/idType/ticker/idValue/${price.instrument.ticker}/"/>">${price.instrument.ticker}</a>&nbsp;</td>
				<td><a href="<c:url value="/prices/latest/idType/ric/idValue/${price.instrument.ric}/"/>">${price.instrument.ric}</a>&nbsp;</td>
				<td><a href="<c:url value="/prices/latest/idType/sedol/idValue/${price.instrument.sedol}/"/>">${price.instrument.sedol}</a>&nbsp;</td>
				<td><a href="<c:url value="/prices/latest/idType/cusip/idValue/${price.instrument.cusip}/"/>">${price.instrument.cusip}</a>&nbsp;</td>
				<td><a href="<c:url value="/prices/latest/idType/isin/idValue/${price.instrument.isin}/"/>">${price.instrument.isin}</a>&nbsp;</td>
				<td><a href="<c:url value="/prices/latest/idType/bbgid/idValue/${price.instrument.bbgid}/"/>">${price.instrument.bbgid}</a>&nbsp;</td>
				<td>${price.price}&nbsp;</td>
				<td><a href="<c:url value="/prices/latest/vendorName/${price.vendor.name}/"/>">${price.vendor.name}</a>&nbsp;</td>
				<td>${price.createTime}&nbsp;</td>
			</tr>
		</c:forEach>
	</table>
	<table>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><a href="<c:url value="/prices/new"/>">New Price</a></td>
		<td>&nbsp;|&nbsp;</td>
		<td><a href="<c:url value="/prices/latest/all"/>">All Latest Prices</a></td>
	</tr>
	</table>
</body>
</html>
