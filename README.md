# Matías Esteban Marín Chacón - INGENIERÍA DE SISTEMAS - ESCUELA TIC - Universidad Piloto de Colombia

Se decide incorporar la anotación @OneToMany en la entidad Competitor, ya que nos permite establecer una relación uno a muchos con la entidad Producto, lo que indica que un competidor tiene asociados varios productos. En la base de datos, esto se refleja en la tabla Producto, que incluye una clave foránea que referencia al competidor, garantizando así la integridad referencial. Además, en caso de configurar el atributo de cascada, las operaciones realizadas sobre el competidor, como la persistencia y la eliminación, se propagarían automáticamente a los productos asociados, haciendo más ágil la gestión y fortaleciendo la relación entre ambas entidades.
## JAX-RS Template Application

This is a template for a lightweight RESTful API using JAX-RS. The sample code is a call for getting the current time.
    
### Running the application locally

First build with:

    $mvn clean install

Then run it with:

    $ java -cp target/classes:target/dependency/* com.example.Main
