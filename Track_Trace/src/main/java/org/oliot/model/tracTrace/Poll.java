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
 * Java class for Poll complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Poll">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="params" type="{query.epcis.oliot.org}QueryParams"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Poll", namespace = "query.epcis.oliot.org", propOrder = {
		"queryName", "params" })
public class Poll {

	@XmlElement(required = true)
	protected String queryName;
	@XmlElement(required = true)
	protected QueryParams params;

	/**
	 * Gets the value of the queryName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * Sets the value of the queryName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setQueryName(String value) {
		this.queryName = value;
	}

	/**
	 * Gets the value of the params property.
	 * 
	 * @return possible object is {@link QueryParams }
	 * 
	 */
	public QueryParams getParams() {
		return params;
	}

	/**
	 * Sets the value of the params property.
	 * 
	 * @param value
	 *            allowed object is {@link QueryParams }
	 * 
	 */
	public void setParams(QueryParams value) {
		this.params = value;
	}

}
