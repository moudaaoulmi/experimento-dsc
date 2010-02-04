package lancs.mobilemedia.optional.pim;

import java.util.Enumeration;

import javax.microedition.pim.*;
import javax.microedition.lcdui.*;

import lancs.mobilemedia.core.ui.MainUIMidlet;
import lancs.mobilemedia.core.ui.controller.MediaListController;
import lancs.mobilemedia.core.ui.datamodel.AlbumData;

import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

public class PimSeed {
	
	MainUIMidlet mid;
	private Enumeration contacts = null;
	private ContactList contList = null;
	
	Command addImg = new Command("Add Image",Command.SCREEN,0);
	Command cancel = new Command("Cancel Image",Command.CANCEL,1);
	
	MediaListController mediaList = null;
	AlbumData model=null;
	
	
	public PimSeed(){
		seed();
	}
		
	
	public void seed() throws PIMException {
		internalSeed();
	
		if(!contacts.hasMoreElements()){
			addContact(contList,"Paul","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"George","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Michael","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Mary","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Nicholas","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Tom","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Russel","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Will","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Michelle","Goldburg","2345 High Park Ave","Orlando","USA","32817");
//			addContact(contList,"Sharon","Goldburg","2345 High Park Ave","Orlando","USA","32817");
		}
		if(contList!=null)
			contList.close();
		contList = null;
		contacts=null;
	}


	private void internalSeed() throws PIMException {
		PIM pimInst = PIM.getInstance();
		contList = (ContactList) pimInst.openPIMList(PIM.CONTACT_LIST, PIM.READ_WRITE);
		contacts = contList.items();
	}
	
	private void addContact (ContactList list, String firstName, String lastName,
	String street, String city, String country, String postalcode) throws PIMException {
		Contact ct = list.createContact();
		String[] name = new String[contList.stringArraySize(Contact.NAME)];
		name[Contact.NAME_GIVEN]=firstName;
		name[Contact.NAME_FAMILY]=lastName;
		ct.addStringArray(Contact.NAME, Contact.ATTR_NONE, name);
		String [] addr = new String[contList.stringArraySize(Contact.ADDR)];
		addr[Contact.ADDR_STREET]=street;
		addr[Contact.ADDR_LOCALITY]=city;
		addr[Contact.ADDR_COUNTRY]=country;
		addr[Contact.ADDR_POSTALCODE]=postalcode;
		ct.addStringArray(Contact.ADDR, Contact.ATTR_NONE, addr);
		ct.commit();
		if (contList != null)
	        contList.close();
	      contList = null;
	}

}