package com.sks.tariff_01.unmarshiling;

//import com.rometools.rome.feed.synd.SyndEntry;
import com.sks.tariff_01.model.ExciseTaxModel;
import com.sks.tariff_01.model.GstCodeDetail;
import com.sks.tariff_01.model.TariffModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
//import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
//import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Primary
public class UnMarsh {

    public GstCodeDetail unmarshalGstDetails(String xmlString) throws Exception {
        Node propertiesNode = XmlBuilder(xmlString);
        JAXBContext context = JAXBContext.newInstance(GstCodeDetail.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (GstCodeDetail) unmarshaller.unmarshal(new DOMSource(propertiesNode));
    }

    public TariffModel unmarshalTariffDetails(String entry) throws Exception {
        Node propertiesNode = XmlBuilder(entry);
        JAXBContext context = JAXBContext.newInstance(TariffModel.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (TariffModel) unmarshaller.unmarshal(new DOMSource(propertiesNode));
    }

    public ExciseTaxModel unmarshalExciseTaxDetails(String entry) throws Exception {
        Node propertiesNode = XmlBuilder(entry);
        JAXBContext context = JAXBContext.newInstance(ExciseTaxModel.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (ExciseTaxModel) unmarshaller.unmarshal(new DOMSource(propertiesNode));
    }

    private static Node XmlBuilder(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        XPath xpath = XPathFactory.newInstance().newXPath();
        return (Node) xpath.evaluate("//*[local-name()='properties']", doc, XPathConstants.NODE);
    }

    public ExciseTaxModel unmarshalExciseTax(String rawXml) throws Exception {
        Node propertiesNode = XmlBuilder(rawXml);
        JAXBContext context = JAXBContext.newInstance(ExciseTaxModel.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (ExciseTaxModel) unmarshaller.unmarshal(new DOMSource(propertiesNode));

    }
}