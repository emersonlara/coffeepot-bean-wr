coffeepot-bean-wr (Writer and Reader)
=====================================

Coffeepot-bean-wr is a simple Java framework for marshalling Java beans to flat file and unmarshal flat file to Java Beans.

Features:

  - Support for delimited and fixed length formats.
  - Uses Java annotations for mapping.
  - Maps unannotated classes dynamically.
  - Override annotations dynamically.
  - Custom type handlers are supported.

Real examples of use you will find them in [coffeepot-br-sped-fiscal](https://github.com/jean-merelis/coffeepot-br-sped-fiscal) and [coffeepot-br-sintegra](https://github.com/jean-merelis/coffeepot-br-sintegra) projects. The coffeepot-br-sped-fiscal project uses delimited format, and the sintegra project uses FixedLength format.


## Version 2.0.0

In version 2, the type handlers are enhanced with the command feature.

Example:

```java
	// DefaultStringHandler has the built-in replace command
	@Field(name="descr", commands={@Cmd(name=DefaultStringHandler.CMD_REPLACE, args={"foo", "bar"} )})
```

Download from Maven central

```xml
        <dependency>
            <groupId>com.github.jean-merelis</groupId>
            <artifactId>coffeepot-bean-wr</artifactId>
            <version>2.0.0-rc.1</version>            
        </dependency> 
```


## Version 1.3.0

Download from Maven central

```xml
        <dependency>
            <groupId>com.github.jean-merelis</groupId>
            <artifactId>coffeepot-bean-wr</artifactId>
            <version>1.3.0</version>            
        </dependency> 
```

Examples
--------

Mapping
-------

```java
		@Record(fields = {
				@Field(name = "", id = true, constantValue = "ORDER"),
				@Field(name = "id"),
				@Field(name = "date"),
				@Field(name = "customer", params = {DefaultStringHandler.CMD_REPLACE, "Sr.", "", "CharCase.UPPER"}),
				@Field(name = "items")
			})
		public class Order {

			private Integer id;
			private Date date;
			private String customer;
			private List<Item> items;
		
			// getters and setters...
		}

		@Record(fields = {
				@Field(name = "", id = true, constantValue = "ITEM"),
				@Field(name = "number"),
				@Field(name = "product"),
				@Field(name = "quantity")
			})
		public class Item {

			private int number;
			private String product;
			private double quantity;
		
			// getters ... setters...
		}		
```

Using...

```java

	public void test() throws Exception{
		Order order = new Order();
		order.setCustomer("Sr. John B.");
		//add some data

        File file = new File("ORDER.tmp");
        Writer w = new FileWriter(file);

        DelimitedWriter instance = new DelimitedWriter(w);
        instance.setDelimiter('|');
        instance.setEscape('\\');
        instance.setRecordTerminator("|\r\n");
        
        instance.write(order);

        w.flush();
        w.close();

```

Output: 

		ORDER|123|2015-03-10T00:04:15|JOHN B.|
		ITEM|1|product 1|10|
		ITEM|2|product 2|5|
		ITEM|3|product 3|2|

    
Reading
--------------

For simple file structure, i.e. where the file has only one type of record, simply map the fields of the class that represents this record and perform the reading. 

If file contains a list of these records you can read with the parseAsListOf.

```java
	//Example for a file with a list of the records.

    public void read(){
		File file = new File("file-with-a-list-of-records.txt");

		try (FileReader fr = new FileReader(file)) {
			FixedLengthReader reader = new FixedLengthReader(fr);
			List<MyRecord> records = reader.parseAsListOf(MyRecord.class);
			for (MyRecord r: records){ 
			  ...
			}
		}

	}
```

For more complex structures that have various types of records, it is necessary that the records have an identifier field so it can be possible to instantiate the corresponding class of the record.
The identifier field must have the value that identifies the record in the constantValue property and the id property must be true.

```java

@Record(fields = {
    @Field(name = "", constantValue = "01", id = true, length = 2), // identifier field
    @Field(name = "value", length = 18)
	// ... more fields
})

public class Rec01 {

    private String value;
		
	// more fields getters and setters...
	...
}


@Record(fields = {
    @Field(name = "", constantValue = "AA", id = true, length = 2), // identifier field
    @Field(name = "value", length = 18)
	// ... more fields
})

public class RecAA {

    private String value;
		
	// more fields getters and setters...
	...
}
```
