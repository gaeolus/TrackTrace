//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.31 at 10:36:23 AM KST 
//

package org.oliot.model.tracTrace;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for EPCISQueryBodyType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="EPCISQueryBodyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{query.epcis.oliot.org}GetQueryNames"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetQueryNamesResult"/>
 *         &lt;element ref="{query.epcis.oliot.org}Subscribe"/>
 *         &lt;element ref="{query.epcis.oliot.org}SubscribeResult"/>
 *         &lt;element ref="{query.epcis.oliot.org}Unsubscribe"/>
 *         &lt;element ref="{query.epcis.oliot.org}UnsubscribeResult"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetSubscriptionIDs"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetSubscriptionIDsResult"/>
 *         &lt;element ref="{query.epcis.oliot.org}Poll"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetStandardVersion"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetStandardVersionResult"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetVendorVersion"/>
 *         &lt;element ref="{query.epcis.oliot.org}GetVendorVersionResult"/>
 *         &lt;element ref="{query.epcis.oliot.org}DuplicateNameException"/>
 *         &lt;element ref="{query.epcis.oliot.org}InvalidURIException"/>
 *         &lt;element ref="{query.epcis.oliot.org}NoSuchNameException"/>
 *         &lt;element ref="{query.epcis.oliot.org}NoSuchSubscriptionException"/>
 *         &lt;element ref="{query.epcis.oliot.org}DuplicateSubscriptionException"/>
 *         &lt;element ref="{query.epcis.oliot.org}QueryParameterException"/>
 *         &lt;element ref="{query.epcis.oliot.org}QueryTooLargeException"/>
 *         &lt;element ref="{query.epcis.oliot.org}QueryTooComplexException"/>
 *         &lt;element ref="{query.epcis.oliot.org}SubscriptionControlsException"/>
 *         &lt;element ref="{query.epcis.oliot.org}SubscribeNotPermittedException"/>
 *         &lt;element ref="{query.epcis.oliot.org}SecurityException"/>
 *         &lt;element ref="{query.epcis.oliot.org}ValidationException"/>
 *         &lt;element ref="{query.epcis.oliot.org}ImplementationException"/>
 *         &lt;element ref="{query.epcis.oliot.org}QueryResults"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EPCISQueryBodyType", namespace = "query.epcis.oliot.org", propOrder = {
		"getQueryNames", "getQueryNamesResult", "subscribe", "subscribeResult",
		"unsubscribe", "unsubscribeResult", "getSubscriptionIDs",
		"getSubscriptionIDsResult", "poll", "getStandardVersion",
		"getStandardVersionResult", "getVendorVersion",
		"getVendorVersionResult", "duplicateNameException",
		"invalidURIException", "noSuchNameException",
		"noSuchSubscriptionException", "duplicateSubscriptionException",
		"queryParameterException", "queryTooLargeException",
		"queryTooComplexException", "subscriptionControlsException",
		"subscribeNotPermittedException", "securityException",
		"validationException", "implementationException", "queryResults" })
public class EPCISQueryBody {

	@XmlElement(name = "GetQueryNames", namespace = "query.epcis.oliot.org")
	protected EmptyParms getQueryNames;
	@XmlElement(name = "GetQueryNamesResult", namespace = "query.epcis.oliot.org")
	protected ArrayOfString getQueryNamesResult;
	@XmlElement(name = "Subscribe", namespace = "query.epcis.oliot.org")
	protected Subscribe subscribe;
	@XmlElement(name = "SubscribeResult", namespace = "query.epcis.oliot.org")
	protected VoidHolder subscribeResult;
	@XmlElement(name = "Unsubscribe", namespace = "query.epcis.oliot.org")
	protected Unsubscribe unsubscribe;
	@XmlElement(name = "UnsubscribeResult", namespace = "query.epcis.oliot.org")
	protected VoidHolder unsubscribeResult;
	@XmlElement(name = "GetSubscriptionIDs", namespace = "query.epcis.oliot.org")
	protected GetSubscriptionIDs getSubscriptionIDs;
	@XmlElement(name = "GetSubscriptionIDsResult", namespace = "query.epcis.oliot.org")
	protected ArrayOfString getSubscriptionIDsResult;
	@XmlElement(name = "Poll", namespace = "query.epcis.oliot.org")
	protected Poll poll;
	@XmlElement(name = "GetStandardVersion", namespace = "query.epcis.oliot.org")
	protected EmptyParms getStandardVersion;
	@XmlElement(name = "GetStandardVersionResult", namespace = "query.epcis.oliot.org")
	protected String getStandardVersionResult;
	@XmlElement(name = "GetVendorVersion", namespace = "query.epcis.oliot.org")
	protected EmptyParms getVendorVersion;
	@XmlElement(name = "GetVendorVersionResult", namespace = "query.epcis.oliot.org")
	protected String getVendorVersionResult;
	@XmlElement(name = "DuplicateNameException", namespace = "query.epcis.oliot.org")
	protected DuplicateNameException duplicateNameException;
	@XmlElement(name = "InvalidURIException", namespace = "query.epcis.oliot.org")
	protected InvalidURIException invalidURIException;
	@XmlElement(name = "NoSuchNameException", namespace = "query.epcis.oliot.org")
	protected NoSuchNameException noSuchNameException;
	@XmlElement(name = "NoSuchSubscriptionException", namespace = "query.epcis.oliot.org")
	protected NoSuchSubscriptionException noSuchSubscriptionException;
	@XmlElement(name = "DuplicateSubscriptionException", namespace = "query.epcis.oliot.org")
	protected DuplicateSubscriptionException duplicateSubscriptionException;
	@XmlElement(name = "QueryParameterException", namespace = "query.epcis.oliot.org")
	protected QueryParameterException queryParameterException;
	@XmlElement(name = "QueryTooLargeException", namespace = "query.epcis.oliot.org")
	protected QueryTooLargeException queryTooLargeException;
	@XmlElement(name = "QueryTooComplexException", namespace = "query.epcis.oliot.org")
	protected QueryTooComplexException queryTooComplexException;
	@XmlElement(name = "SubscriptionControlsException", namespace = "query.epcis.oliot.org")
	protected SubscriptionControlsException subscriptionControlsException;
	@XmlElement(name = "SubscribeNotPermittedException", namespace = "query.epcis.oliot.org")
	protected SubscribeNotPermittedException subscribeNotPermittedException;
	@XmlElement(name = "SecurityException", namespace = "query.epcis.oliot.org")
	protected SecurityException securityException;
	@XmlElement(name = "ValidationException", namespace = "query.epcis.oliot.org")
	protected ValidationException validationException;
	@XmlElement(name = "ImplementationException", namespace = "query.epcis.oliot.org")
	protected ImplementationException implementationException;
	@XmlElement(name = "QueryResults", namespace = "query.epcis.oliot.org")
	protected QueryResults queryResults;

	/**
	 * Gets the value of the getQueryNames property.
	 * 
	 * @return possible object is {@link EmptyParms }
	 * 
	 */
	public EmptyParms getGetQueryNames() {
		return getQueryNames;
	}

	/**
	 * Sets the value of the getQueryNames property.
	 * 
	 * @param value
	 *            allowed object is {@link EmptyParms }
	 * 
	 */
	public void setGetQueryNames(EmptyParms value) {
		this.getQueryNames = value;
	}

	/**
	 * Gets the value of the getQueryNamesResult property.
	 * 
	 * @return possible object is {@link ArrayOfString }
	 * 
	 */
	public ArrayOfString getGetQueryNamesResult() {
		return getQueryNamesResult;
	}

	/**
	 * Sets the value of the getQueryNamesResult property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfString }
	 * 
	 */
	public void setGetQueryNamesResult(ArrayOfString value) {
		this.getQueryNamesResult = value;
	}

	/**
	 * Gets the value of the subscribe property.
	 * 
	 * @return possible object is {@link Subscribe }
	 * 
	 */
	public Subscribe getSubscribe() {
		return subscribe;
	}

	/**
	 * Sets the value of the subscribe property.
	 * 
	 * @param value
	 *            allowed object is {@link Subscribe }
	 * 
	 */
	public void setSubscribe(Subscribe value) {
		this.subscribe = value;
	}

	/**
	 * Gets the value of the subscribeResult property.
	 * 
	 * @return possible object is {@link VoidHolder }
	 * 
	 */
	public VoidHolder getSubscribeResult() {
		return subscribeResult;
	}

	/**
	 * Sets the value of the subscribeResult property.
	 * 
	 * @param value
	 *            allowed object is {@link VoidHolder }
	 * 
	 */
	public void setSubscribeResult(VoidHolder value) {
		this.subscribeResult = value;
	}

	/**
	 * Gets the value of the unsubscribe property.
	 * 
	 * @return possible object is {@link Unsubscribe }
	 * 
	 */
	public Unsubscribe getUnsubscribe() {
		return unsubscribe;
	}

	/**
	 * Sets the value of the unsubscribe property.
	 * 
	 * @param value
	 *            allowed object is {@link Unsubscribe }
	 * 
	 */
	public void setUnsubscribe(Unsubscribe value) {
		this.unsubscribe = value;
	}

	/**
	 * Gets the value of the unsubscribeResult property.
	 * 
	 * @return possible object is {@link VoidHolder }
	 * 
	 */
	public VoidHolder getUnsubscribeResult() {
		return unsubscribeResult;
	}

	/**
	 * Sets the value of the unsubscribeResult property.
	 * 
	 * @param value
	 *            allowed object is {@link VoidHolder }
	 * 
	 */
	public void setUnsubscribeResult(VoidHolder value) {
		this.unsubscribeResult = value;
	}

	/**
	 * Gets the value of the getSubscriptionIDs property.
	 * 
	 * @return possible object is {@link GetSubscriptionIDs }
	 * 
	 */
	public GetSubscriptionIDs getGetSubscriptionIDs() {
		return getSubscriptionIDs;
	}

	/**
	 * Sets the value of the getSubscriptionIDs property.
	 * 
	 * @param value
	 *            allowed object is {@link GetSubscriptionIDs }
	 * 
	 */
	public void setGetSubscriptionIDs(GetSubscriptionIDs value) {
		this.getSubscriptionIDs = value;
	}

	/**
	 * Gets the value of the getSubscriptionIDsResult property.
	 * 
	 * @return possible object is {@link ArrayOfString }
	 * 
	 */
	public ArrayOfString getGetSubscriptionIDsResult() {
		return getSubscriptionIDsResult;
	}

	/**
	 * Sets the value of the getSubscriptionIDsResult property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfString }
	 * 
	 */
	public void setGetSubscriptionIDsResult(ArrayOfString value) {
		this.getSubscriptionIDsResult = value;
	}

	/**
	 * Gets the value of the poll property.
	 * 
	 * @return possible object is {@link Poll }
	 * 
	 */
	public Poll getPoll() {
		return poll;
	}

	/**
	 * Sets the value of the poll property.
	 * 
	 * @param value
	 *            allowed object is {@link Poll }
	 * 
	 */
	public void setPoll(Poll value) {
		this.poll = value;
	}

	/**
	 * Gets the value of the getStandardVersion property.
	 * 
	 * @return possible object is {@link EmptyParms }
	 * 
	 */
	public EmptyParms getGetStandardVersion() {
		return getStandardVersion;
	}

	/**
	 * Sets the value of the getStandardVersion property.
	 * 
	 * @param value
	 *            allowed object is {@link EmptyParms }
	 * 
	 */
	public void setGetStandardVersion(EmptyParms value) {
		this.getStandardVersion = value;
	}

	/**
	 * Gets the value of the getStandardVersionResult property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGetStandardVersionResult() {
		return getStandardVersionResult;
	}

	/**
	 * Sets the value of the getStandardVersionResult property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGetStandardVersionResult(String value) {
		this.getStandardVersionResult = value;
	}

	/**
	 * Gets the value of the getVendorVersion property.
	 * 
	 * @return possible object is {@link EmptyParms }
	 * 
	 */
	public EmptyParms getGetVendorVersion() {
		return getVendorVersion;
	}

	/**
	 * Sets the value of the getVendorVersion property.
	 * 
	 * @param value
	 *            allowed object is {@link EmptyParms }
	 * 
	 */
	public void setGetVendorVersion(EmptyParms value) {
		this.getVendorVersion = value;
	}

	/**
	 * Gets the value of the getVendorVersionResult property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGetVendorVersionResult() {
		return getVendorVersionResult;
	}

	/**
	 * Sets the value of the getVendorVersionResult property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGetVendorVersionResult(String value) {
		this.getVendorVersionResult = value;
	}

	/**
	 * Gets the value of the duplicateNameException property.
	 * 
	 * @return possible object is {@link DuplicateNameException }
	 * 
	 */
	public DuplicateNameException getDuplicateNameException() {
		return duplicateNameException;
	}

	/**
	 * Sets the value of the duplicateNameException property.
	 * 
	 * @param value
	 *            allowed object is {@link DuplicateNameException }
	 * 
	 */
	public void setDuplicateNameException(DuplicateNameException value) {
		this.duplicateNameException = value;
	}

	/**
	 * Gets the value of the invalidURIException property.
	 * 
	 * @return possible object is {@link InvalidURIException }
	 * 
	 */
	public InvalidURIException getInvalidURIException() {
		return invalidURIException;
	}

	/**
	 * Sets the value of the invalidURIException property.
	 * 
	 * @param value
	 *            allowed object is {@link InvalidURIException }
	 * 
	 */
	public void setInvalidURIException(InvalidURIException value) {
		this.invalidURIException = value;
	}

	/**
	 * Gets the value of the noSuchNameException property.
	 * 
	 * @return possible object is {@link NoSuchNameException }
	 * 
	 */
	public NoSuchNameException getNoSuchNameException() {
		return noSuchNameException;
	}

	/**
	 * Sets the value of the noSuchNameException property.
	 * 
	 * @param value
	 *            allowed object is {@link NoSuchNameException }
	 * 
	 */
	public void setNoSuchNameException(NoSuchNameException value) {
		this.noSuchNameException = value;
	}

	/**
	 * Gets the value of the noSuchSubscriptionException property.
	 * 
	 * @return possible object is {@link NoSuchSubscriptionException }
	 * 
	 */
	public NoSuchSubscriptionException getNoSuchSubscriptionException() {
		return noSuchSubscriptionException;
	}

	/**
	 * Sets the value of the noSuchSubscriptionException property.
	 * 
	 * @param value
	 *            allowed object is {@link NoSuchSubscriptionException }
	 * 
	 */
	public void setNoSuchSubscriptionException(NoSuchSubscriptionException value) {
		this.noSuchSubscriptionException = value;
	}

	/**
	 * Gets the value of the duplicateSubscriptionException property.
	 * 
	 * @return possible object is {@link DuplicateSubscriptionException }
	 * 
	 */
	public DuplicateSubscriptionException getDuplicateSubscriptionException() {
		return duplicateSubscriptionException;
	}

	/**
	 * Sets the value of the duplicateSubscriptionException property.
	 * 
	 * @param value
	 *            allowed object is {@link DuplicateSubscriptionException }
	 * 
	 */
	public void setDuplicateSubscriptionException(
			DuplicateSubscriptionException value) {
		this.duplicateSubscriptionException = value;
	}

	/**
	 * Gets the value of the queryParameterException property.
	 * 
	 * @return possible object is {@link QueryParameterException }
	 * 
	 */
	public QueryParameterException getQueryParameterException() {
		return queryParameterException;
	}

	/**
	 * Sets the value of the queryParameterException property.
	 * 
	 * @param value
	 *            allowed object is {@link QueryParameterException }
	 * 
	 */
	public void setQueryParameterException(QueryParameterException value) {
		this.queryParameterException = value;
	}

	/**
	 * Gets the value of the queryTooLargeException property.
	 * 
	 * @return possible object is {@link QueryTooLargeException }
	 * 
	 */
	public QueryTooLargeException getQueryTooLargeException() {
		return queryTooLargeException;
	}

	/**
	 * Sets the value of the queryTooLargeException property.
	 * 
	 * @param value
	 *            allowed object is {@link QueryTooLargeException }
	 * 
	 */
	public void setQueryTooLargeException(QueryTooLargeException value) {
		this.queryTooLargeException = value;
	}

	/**
	 * Gets the value of the queryTooComplexException property.
	 * 
	 * @return possible object is {@link QueryTooComplexException }
	 * 
	 */
	public QueryTooComplexException getQueryTooComplexException() {
		return queryTooComplexException;
	}

	/**
	 * Sets the value of the queryTooComplexException property.
	 * 
	 * @param value
	 *            allowed object is {@link QueryTooComplexException }
	 * 
	 */
	public void setQueryTooComplexException(QueryTooComplexException value) {
		this.queryTooComplexException = value;
	}

	/**
	 * Gets the value of the subscriptionControlsException property.
	 * 
	 * @return possible object is {@link SubscriptionControlsException }
	 * 
	 */
	public SubscriptionControlsException getSubscriptionControlsException() {
		return subscriptionControlsException;
	}

	/**
	 * Sets the value of the subscriptionControlsException property.
	 * 
	 * @param value
	 *            allowed object is {@link SubscriptionControlsException }
	 * 
	 */
	public void setSubscriptionControlsException(
			SubscriptionControlsException value) {
		this.subscriptionControlsException = value;
	}

	/**
	 * Gets the value of the subscribeNotPermittedException property.
	 * 
	 * @return possible object is {@link SubscribeNotPermittedException }
	 * 
	 */
	public SubscribeNotPermittedException getSubscribeNotPermittedException() {
		return subscribeNotPermittedException;
	}

	/**
	 * Sets the value of the subscribeNotPermittedException property.
	 * 
	 * @param value
	 *            allowed object is {@link SubscribeNotPermittedException }
	 * 
	 */
	public void setSubscribeNotPermittedException(
			SubscribeNotPermittedException value) {
		this.subscribeNotPermittedException = value;
	}

	/**
	 * Gets the value of the securityException property.
	 * 
	 * @return possible object is {@link SecurityException }
	 * 
	 */
	public SecurityException getSecurityException() {
		return securityException;
	}

	/**
	 * Sets the value of the securityException property.
	 * 
	 * @param value
	 *            allowed object is {@link SecurityException }
	 * 
	 */
	public void setSecurityException(SecurityException value) {
		this.securityException = value;
	}

	/**
	 * Gets the value of the validationException property.
	 * 
	 * @return possible object is {@link ValidationException }
	 * 
	 */
	public ValidationException getValidationException() {
		return validationException;
	}

	/**
	 * Sets the value of the validationException property.
	 * 
	 * @param value
	 *            allowed object is {@link ValidationException }
	 * 
	 */
	public void setValidationException(ValidationException value) {
		this.validationException = value;
	}

	/**
	 * Gets the value of the implementationException property.
	 * 
	 * @return possible object is {@link ImplementationException }
	 * 
	 */
	public ImplementationException getImplementationException() {
		return implementationException;
	}

	/**
	 * Sets the value of the implementationException property.
	 * 
	 * @param value
	 *            allowed object is {@link ImplementationException }
	 * 
	 */
	public void setImplementationException(ImplementationException value) {
		this.implementationException = value;
	}

	/**
	 * Gets the value of the queryResults property.
	 * 
	 * @return possible object is {@link QueryResults }
	 * 
	 */
	public QueryResults getQueryResults() {
		return queryResults;
	}

	/**
	 * Sets the value of the queryResults property.
	 * 
	 * @param value
	 *            allowed object is {@link QueryResults }
	 * 
	 */
	public void setQueryResults(QueryResults value) {
		this.queryResults = value;
	}

}
