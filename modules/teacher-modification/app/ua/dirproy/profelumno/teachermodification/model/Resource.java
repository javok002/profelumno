package ua.dirproy.profelumno.teachermodification.model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by rudy on 24/09/15.
 */
@Entity
public class Resource extends Model {

    @Id
    private long id;

    private String path="";
    private String name;
    private String type;

    private File file;

    public Resource (String fileName, File file, String type){
        this.name=fileName;
        this.file=file;
        this.type=type;
    }

    public Resource(){}

    public static Finder<Long, Resource> find = new Finder<Long, Resource>(Long.class, Resource.class);

    public static Resource getById(long id){
        return find.byId(id);
    }

    public static void copyFile(File sourceFIle, File destinationFile) throws IOException{
        if (destinationFile.exists())
            destinationFile.delete();
        FileInputStream fileInputStream=null;
        try{
            fileInputStream=new FileInputStream(sourceFIle);
            FileOutputStream fileOutputStream= new FileOutputStream(destinationFile);
            int byte1 =fileInputStream.read();
            while(byte1!=-1){
                fileOutputStream.write(byte1);
                byte1=fileInputStream.read();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        if (file==null)
            file=new File(path);
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
