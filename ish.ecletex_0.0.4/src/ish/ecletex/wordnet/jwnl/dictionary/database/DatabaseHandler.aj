package ish.ecletex.wordnet.jwnl.dictionary.database;

import ish.ecletex.wordnet.jwnl.JWNLException;
import ish.ecletex.wordnet.jwnl.data.POS;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public privileged aspect DatabaseHandler {

	Map queryMap = new HashMap();

	pointcut internalRegisterDriverHandler(): execution(private void internalRegisterDriver());

	pointcut internalGetRandomIndexWordQueryHandler(): execution(private DatabaseManagerImpl.MinMax internalGetRandomIndexWordQuery(POS , DatabaseManagerImpl.MinMax, Query));

	pointcut internalCreatePOSQueryHandler(): execution(private Query DatabaseManagerImpl.internalCreatePOSQuery(POS , String , Query));

	pointcut getQueryHandler(): call(public Query ConnectionManager.getQuery(String));

	pointcut internalCreatePOSStringQueryHandler(): execution(private Query DatabaseManagerImpl.internalCreatePOSStringQuery(POS , String , String ,	Query ));

	pointcut internalCreatePOSOffsetQueryHandler(): execution(private Query DatabaseManagerImpl.internalCreatePOSOffsetQuery(POS , long ,String , Query ));

	pointcut internalCreatePOSIdQueryHandler(): execution(private Query DatabaseManagerImpl.internalCreatePOSIdQuery(POS , int , String ,Query ) );


	declare soft: Exception : internalRegisterDriverHandler();
	declare soft: SQLException : internalGetRandomIndexWordQueryHandler() || internalCreatePOSQueryHandler() || internalCreatePOSStringQueryHandler() || internalCreatePOSOffsetQueryHandler() || internalCreatePOSIdQueryHandler();

	

	Query around() throws JWNLException : internalCreatePOSIdQueryHandler() || internalCreatePOSOffsetQueryHandler() || internalCreatePOSStringQueryHandler() || internalCreatePOSQueryHandler()  {
		try {
			return proceed();
		} catch (SQLException ex) {
			Query query = (Query) this.queryMap.get(Thread.currentThread()
					.getName());
			if (query != null) {
				query.close();
			}
			throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
		}
	}

	Query around(): getQueryHandler()&& (withincode(private Query DatabaseManagerImpl.internalCreatePOSQuery(POS , String , Query)) || withincode(private Query DatabaseManagerImpl.internalCreatePOSStringQuery(POS , String , String , Query )) || withincode(private Query DatabaseManagerImpl.internalCreatePOSOffsetQuery(POS , long ,String , Query)) || withincode(private Query DatabaseManagerImpl.internalCreatePOSIdQuery(POS , int , String ,Query ) )){
		Query q = proceed();
		this.queryMap.put(Thread.currentThread().getName(), q);
		return q;
	}

	Object around(Query query) throws JWNLException : internalGetRandomIndexWordQueryHandler() && args(..,query)  {
		try {
			return proceed(query);
		} catch (SQLException ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_023", ex);
		} finally {
			if (query != null) {
				query.close();
			}
		}
	}

	void around() throws JWNLException : internalRegisterDriverHandler() {
		try {
			proceed();
		} catch (Exception ex) {
			throw new JWNLException("DICTIONARY_EXCEPTION_024", ex);
		}
	}

}
