package se.yrgo.services.calls;

import java.util.*;

import se.yrgo.domain.*;
import se.yrgo.services.customers.*;
import se.yrgo.services.diary.*;

public class CallHandlingServiceImpl implements CallHandlingService {

    private CustomerManagementService cms;
    private DiaryManagementService dms;

    public CallHandlingServiceImpl(CustomerManagementService cms, DiaryManagementService dms) {
        this.cms = cms;
        this.dms = dms;
    }

    @Override
    public void recordCall(String customerId, Call newCall, Collection<Action> actions)
            throws CustomerNotFoundException {

                cms.recordCall(customerId, newCall);

                for (Action item : actions) {
                    dms.recordAction(item);
                }
                
    }
    
}
