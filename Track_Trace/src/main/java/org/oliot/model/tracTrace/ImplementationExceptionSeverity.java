//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.31 at 10:36:23 AM KST 
//

package org.oliot.model.tracTrace;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ImplementationExceptionSeverity.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="ImplementationExceptionSeverity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *     &lt;enumeration value="ERROR"/>
 *     &lt;enumeration value="SEVERE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ImplementationExceptionSeverity", namespace = "query.epcis.oliot.org")
@XmlEnum
public enum ImplementationExceptionSeverity {

	ERROR, SEVERE;

	public String value() {
		return name();
	}

	public static ImplementationExceptionSeverity fromValue(String v) {
		return valueOf(v);
	}

}
