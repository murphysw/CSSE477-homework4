package protocol;

import java.util.Set;
import java.util.TreeSet;

public class HttpResponseValidator {

	private Set<Integer> codes;
	private Set<String> codeTexts;
	
	public HttpResponseValidator() {
		this.codes = new TreeSet<Integer>();
		this.codeTexts = new TreeSet<String>();
		setUpCodes();
		setUpCodeTexts();
	}
	
	public HttpResponse checkResponse(HttpResponse response) {
		if (!codes.contains(response.getStatus()) ||
				!codeTexts.contains(response.getPhrase())) {
			return HttpResponseFactory.create500InternalServerError(Protocol.CLOSE);
		}
		return response;
	}
	
	private void setUpCodes() {
		codes.add(Protocol.BAD_REQUEST_CODE);
		codes.add(Protocol.CREATED_CODE);
		codes.add(Protocol.INTERNAL_SERVER_ERROR);
		codes.add(Protocol.METHOD_NOT_ALLOWED);
		codes.add(Protocol.MOVED_PERMANENTLY_CODE);
		codes.add(Protocol.NOT_FOUND_CODE);
		codes.add(Protocol.NOT_SUPPORTED_CODE);
		codes.add(Protocol.OK_CODE);
		codes.add(Protocol.SERVICE_UNAVAILABLE);
	}
	
	private void setUpCodeTexts() {
		codeTexts.add(Protocol.BAD_REQUEST_TEXT);
		codeTexts.add(Protocol.CREATED_TEXT);
		codeTexts.add(Protocol.INTERNAL_SERVER_ERROR_TEXT);
		codeTexts.add(Protocol.METHOD_NOT_ALLOWED_TEXT);
		codeTexts.add(Protocol.MOVED_PERMANENTLY_TEXT);
		codeTexts.add(Protocol.NOT_FOUND_TEXT);
		codeTexts.add(Protocol.NOT_SUPPORTED_TEXT);
		codeTexts.add(Protocol.OK_TEXT);
		codeTexts.add(Protocol.TEMPORARY_OVERLOAD);
	}
}
