package io.sequence.elastic;

public class ElasticException extends RuntimeException{

	private static final long serialVersionUID = 5415283986899868889L;
	
	public ElasticException(){
		super();
	}
	
	public ElasticException(String message){
		super(message);
	}
	
	public ElasticException(Throwable t) {
        super(t);
    }


    public ElasticException(String message, Throwable t) {
        super(message, t);
    }

}
