package com.phicomm.remotecontrol.modules.main.screenprojection.callback;

import org.fourthline.cling.model.action.ActionException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by kang.sun on 2017/8/22.
 */
public abstract class Browse extends UpnpActionCallback {
    public static final String CAPS_WILDCARD = "*";
    public static final long MAXRESULTS = 999;

    public enum Status {
        NO_CONTENT("No Content"), LOADING("Loading..."), OK("OK");
        private String defaultMessage;

        Status(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }
    }

    private static Logger log = Logger.getLogger(Browse.class.getName());

    public Browse(Service service, String containerId, BrowseFlag flag) {
        this(service, containerId, flag, CAPS_WILDCARD, 0, null);
    }

    public Browse(Service service, String objectID, BrowseFlag flag,
                  String filter, long firstResult, Long maxResults,
                  SortCriterion... orderBy) {
        super(new ActionInvocation(service.getAction("Browse")));
        log.fine("Creating browse action for object ID: " + objectID);
        setInput("ObjectID", objectID);
        setInput("BrowseFlag", flag.toString());
        setInput("Filter", filter);
        setInput("StartingIndex", new UnsignedIntegerFourBytes(firstResult));
        setInput("RequestedCount", new UnsignedIntegerFourBytes(
                maxResults == null ? getDefaultMaxResults() : maxResults));
        getActionInvocation().setInput("SortCriteria",
                SortCriterion.toString(orderBy));
    }

    @Override
    public void run() {
        updateStatus(Status.LOADING);
        super.run();
    }

    public void received(ActionInvocation invocation, Map<String, Object> map) {
        log.fine("Successful browse action, reading output argument values");

        BrowseResult result = new BrowseResult(map.get("Result").toString(),
                (UnsignedIntegerFourBytes) map.get("NumberReturned"),
                (UnsignedIntegerFourBytes) map.get("TotalMatches"),
                (UnsignedIntegerFourBytes) map.get("UpdateID"));
        boolean proceed = receivedRaw(invocation, result);
        if (proceed && result.getCountLong() > 0
                && result.getResult().length() > 0) {
            try {
                DIDLParser didlParser = new DIDLParser();
                DIDLContent didl = didlParser.parse(result.getResult());
                received(invocation, didl);
                updateStatus(Status.OK);
            } catch (Exception ex) {
                invocation.setFailure(new ActionException(
                        ErrorCode.ACTION_FAILED,
                        "Can't parse DIDL XML response: " + ex, ex));
                failure(invocation, null);
            }
        } else {
            received(invocation, new DIDLContent());
            updateStatus(Status.NO_CONTENT);
        }
    }

    public long getDefaultMaxResults() {
        return MAXRESULTS;
    }

    public boolean receivedRaw(ActionInvocation actionInvocation, BrowseResult browseResult) {
        return true;
    }

    public abstract void received(ActionInvocation actionInvocation, DIDLContent didl);

    public abstract void updateStatus(Status status);
}
