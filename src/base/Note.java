package base;
import java.util.Date;
import java.io.Serializable;

public class Note implements Comparable<Note>,Serializable {
    private Date date;
    private String title;
    private static final long serialVersionUID = 1L;
    
    public Note(String title){
    	this.title=title;
    	date = new Date(System.currentTimeMillis());
    }
    
    public String getTitle(){
    	return title;
    }
    

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass().getSuperclass() != obj.getClass().getSuperclass())
			return false;
		Note other = (Note) obj;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(Note o){
		if(this.date.after(o.date)){
			return -1;
		}else if(this.date.before(o.date)){
			return 1;
		}else{
			return 0;
		}
	}
	
	public String toString(){
		return date.toString() + "\t" + title;
	}
}
