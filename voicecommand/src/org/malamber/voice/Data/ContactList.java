package org.malamber.voice.data;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactList extends ArrayList<Contact>
{	
	private static final long serialVersionUID = -484178098162921358L;
	
	/*private Context context;
	
	public ContactList(Context c)
	{
		context = c;
		
	}*/
	
	public static ContactList searchContacts(Context c, String name)
	{
		ContactList al = new ContactList();
		
		Cursor cursor = c.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null); 
		while (cursor.moveToNext())
		{ 
			Contact contact = new Contact(c);
			
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
			String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)); 
			if (Boolean.parseBoolean(hasPhone))
			{ 				
				// You know it has a number so now query it like this
				Cursor phones = c.getContentResolver().query( 
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
						null, 
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, 
						null, null); 
				
				while (phones.moveToNext())
				{ 
					String phoneNumber = phones.getString(phones.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
					contact.number.add(phoneNumber);
				} 
				phones.close(); 
			}
			
			al.add(contact);
			
		}
		
		cursor.close();		
		return al;
	}
}
