package base;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.Serializable;

public class Folder implements Comparable<Folder>,Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Note> notes;
	private String name;
	
	public Folder(String name){
		this.name=name;
		notes = new ArrayList<Note>();
		
	}
	
	public void addNote(Note note){
		
		notes.add(note);
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<Note> getNotes(){
		return notes;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Folder other = (Folder) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	@Override
	public String toString() { 
		int nText = 0;
	    int nImage = 0;
	    // TODO
	    for (Note n : this.notes){
	    	if (n instanceof TextNote){
	    		nText++;
	    	}else{
	    		nImage++;
	    	}
	    }
	    return name + ":" + nText + ":" + nImage; }
	
	@Override
	public int compareTo(Folder o){
		if(this.name.compareTo(o.name)==0){
			return 0;
		}else if(this.name.compareTo(o.name)>0) {
			return 1;
		}else{
			return -1;
		}		
	}
	
	public void sortNotes(){
		
		Collections.sort(notes);
		
	}
	

	
//	public List<Note> searchNotes(String keywords){
//		ArrayList<Note> returnList=new ArrayList<Note>();
//		ArrayList<String> keyarray=new ArrayList<String>();
//		ArrayList<String> wordarray=new ArrayList<String>();
//		for( String word : keywords.split(" ")){
//			keyarray.add(word.toLowerCase());
//		}
//		
//		for (Note n : notes){	
//			for( String word : n.getTitle().split(" ")){
//				wordarray.add(word.toLowerCase());
//			}
//			if(n instanceof TextNote){
//				TextNote textn=(TextNote)n;
//				for( String word : textn.getContent().split(" ")){
//					wordarray.add(word.toLowerCase());
//				}
//			}
//			boolean pass=true;
//			int count=0;
//			int status=0;// 0=neutral 1=previous pass 2=previous miss 3=pass current
//			for(String key: keyarray){
//				count++;
//				
//				if(key.equals("or")){
//					if(status==1){status=3;}
//					if(status==2){status=0;}
//					continue;
//				}else{
//					if(status==1){status=0;}
//					if(status==2){pass=false;break;}
//					if(status==3){status=0;continue;}
//				}
//				for (String word : wordarray){
//					status=2;
//					word=word.toLowerCase();
//					if(key.equals(word)){				
//						status=1;
//						break;
//					}
//				}
//				
//				if(count==keyarray.size()){
//					if(status==2){pass=false;}
//				}
//			}
//			if(pass){
//				returnList.add(n);
//			}
//		}
//		
//		return returnList;
//
//	}
	public List<Note> searchNotes(String keywords){
		List<Note> totalNote = new ArrayList<>();
		keywords = keywords.toLowerCase();
		String[] words = keywords.split(" ");
		boolean correct = false;

			for(Note oneNote :notes){
				String[] oneText;		
				if (oneNote instanceof TextNote){
					TextNote a = (TextNote) oneNote;
					String noteContent = a.getTitle() +" "+ a.content;
					oneText = noteContent.toLowerCase().split(" ");
				}else{
					oneText = oneNote.getTitle().toLowerCase().split(" ");	
				}

				
				for(int j=0;j<words.length; j++){	
					for (int i = 0; i< oneText.length; i++){
						if (words[j].equals(oneText[i])){
							correct = true;
							break;
						}
					}
					if((j+1) < words.length){
						if(words[j+1].equals("or")){
							if(correct == false){
								j=j+1;
								continue;
								}else if((correct == true)){
									if((j+3)< words.length){
										if(words[j+3].equals("or")){
											j=j+4;
											continue;
										}else{
											
										
										j=j+2;
										correct = false;
										continue;
										}
									}else{
										break;	
									}
								}
						}
						if(correct == true){
							correct = false;
							continue;
						}
					}
					if(correct == false){
						break;
					}						
					
				}
				if (correct == true){
					totalNote.add(oneNote);
				}
			}
	return totalNote;
	}

	public boolean removeNotes(String title) {
		// TODO Auto-generated method stub

		for(Note n:this.notes){
			if(n.getTitle().equals(title)){
				
				this.notes.remove(this.notes.indexOf(n));
				return true;
			}     
		}
		
		return false;
	}	
}
