<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div>
	<h2>Submit New Price</h2>

	<sf:form method="POST" modelAttribute="price">
		<fieldset>
			<table cellspacing="0">
				<tr>
					<th><label for="price">Price:</label></th>
					<td><sf:input path="price" size="15" id="price" /></td>
				</tr>
				<tr>
					<th><label for="vendor">Vendor:</label></th>
					<td><sf:input path="vendor.name" size="15" id="vendor" /></td>
				</tr>
			</table>
			<br/>
			<h3>Instrument Identifiers</h3>
			<table>
				<tr>
					<th><label for="ticker">Ticker:</label></th>
					<td><sf:input path="instrument.ticker" size="15" id="ticker" /></td>
				</tr>
				<tr>
					<th><label for="ric">RIC:</label></th>
					<td><sf:input path="instrument.ric" size="15" id="ric" /></td>
				</tr>
				<tr>
					<th><label for="bbgid">BBG ID:</label></th>
					<td><sf:input path="instrument.bbgid" size="15" id="bbgid" /></td>
				</tr>
				<tr>
					<th><label for="isin">ISIN:</label></th>
					<td><sf:input path="instrument.isin" size="15" id="isin" /></td>
				</tr>
				<tr>
					<th><label for="cusip">CUSIP:</label></th>
					<td><sf:input path="instrument.cusip" size="15" id="cusip" /></td>
				</tr>
				<tr>
					<th><label for="sedol">SEDOL:</label></th>
					<td><sf:input path="instrument.sedol" size="15" id="sedol" /></td>
				</tr>
				<tr>
					<th></th>
					<td><input name="commit" type="submit" value="Publish" /></td>
				</tr>

			</table>
		</fieldset>
	</sf:form>
</div>