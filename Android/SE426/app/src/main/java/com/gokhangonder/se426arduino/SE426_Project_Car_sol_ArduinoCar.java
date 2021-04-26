package com.gokhangonder.se426arduino;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class SE426_Project_Car_sol_ArduinoCar extends Contract {
    private static final String BINARY = "60c06040526005608081905264447269766560d81b60a090815261002691600191906100ec565b5060408051808201909152600480825263053746f760e41b6020909201918252610052916002916100ec565b50604080518082019091526004808252631319599d60e21b602090920191825261007e916003916100ec565b5060408051808201909152600580825264149a59da1d60da1b60209092019182526100ab916004916100ec565b503480156100b857600080fd5b50600260059080546100c990610200565b6100d4929190610170565b50600080546001600160a01b0319163317905561023b565b8280546100f890610200565b90600052602060002090601f01602090048101928261011a5760008555610160565b82601f1061013357805160ff1916838001178555610160565b82800160010185558215610160579182015b82811115610160578251825591602001919060010190610145565b5061016c9291506101eb565b5090565b82805461017c90610200565b90600052602060002090601f01602090048101928261019e5760008555610160565b82601f106101af5780548555610160565b8280016001018555821561016057600052602060002091601f016020900482015b828111156101605782548255916001019190600101906101d0565b5b8082111561016c57600081556001016101ec565b600181811c9082168061021457607f821691505b6020821081141561023557634e487b7160e01b600052602260045260246000fd5b50919050565b6103d18061024a6000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80634e6621441461005c578063b1976a0214610066578063bedf0f4a14610084578063ce689d111461008c578063efd1da7f14610094575b600080fd5b61006461009c565b005b61006e6100ba565b60405161007b919061030d565b60405180910390f35b610064610186565b610064610196565b6100646101a6565b600360059080546100ac90610360565b6100b79291906101b6565b50565b60607fb631afd38d08cc6e5ad52296100dfc2f8c45b6d1ce18753db1b3a834f365f3533360056040516100ee929190610256565b60405180910390a16005805461010390610360565b80601f016020809104026020016040519081016040528092919081815260200182805461012f90610360565b801561017c5780601f106101515761010080835404028352916020019161017c565b820191906000526020600020905b81548152906001019060200180831161015f57829003601f168201915b5050505050905090565b600260059080546100ac90610360565b600160059080546100ac90610360565b600460059080546100ac90610360565b8280546101c290610360565b90600052602060002090601f0160209004810192826101e45760008555610231565b82601f106101f55780548555610231565b8280016001018555821561023157600052602060002091601f016020900482015b82811115610231578254825591600101919060010190610216565b5061023d929150610241565b5090565b5b8082111561023d5760008155600101610242565b600060018060a01b0384168252602060408184015281845483600182811c91508083168061028557607f831692505b8583108114156102a357634e487b7160e01b87526022600452602487fd5b60408801839052606088018180156102c257600181146102d3576102fd565b60ff198616825287820196506102fd565b60008b815260209020895b868110156102f7578154848201529085019089016102de565b83019750505b50949a9950505050505050505050565b6000602080835283518082850152825b818110156103395785810183015185820160400152820161031d565b8181111561034a5783604083870101525b50601f01601f1916929092016040019392505050565b600181811c9082168061037457607f821691505b6020821081141561039557634e487b7160e01b600052602260045260246000fd5b5091905056fea2646970667358221220cec003b87b519223dcf32679999d506844c2347edc5a946b1003f4a90f23859a64736f6c63430008030033";

    public static final String FUNC_DRIVE = "Drive";

    public static final String FUNC_GET = "Get";

    public static final String FUNC_LEFT = "Left";

    public static final String FUNC_RIGHT = "Right";

    public static final String FUNC_STOP = "Stop";

    public static final Event RETURNRESULT_EVENT = new Event("ReturnResult",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
            }, new TypeReference<Utf8String>() {
            }));
    ;

    @Deprecated
    protected SE426_Project_Car_sol_ArduinoCar(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SE426_Project_Car_sol_ArduinoCar(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SE426_Project_Car_sol_ArduinoCar(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SE426_Project_Car_sol_ArduinoCar(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<ReturnResultEventResponse> getReturnResultEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(RETURNRESULT_EVENT, transactionReceipt);
        ArrayList<ReturnResultEventResponse> responses = new ArrayList<ReturnResultEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ReturnResultEventResponse typedResponse = new ReturnResultEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._command = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ReturnResultEventResponse> returnResultEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, ReturnResultEventResponse>() {
            @Override
            public ReturnResultEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(RETURNRESULT_EVENT, log);
                ReturnResultEventResponse typedResponse = new ReturnResultEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._command = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ReturnResultEventResponse> returnResultEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(RETURNRESULT_EVENT));
        return returnResultEventFlowable(filter);
    }

    public RemoteCall<TransactionReceipt> Drive() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DRIVE,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> Get() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GET,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> Left() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_LEFT,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> Right() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RIGHT,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> Stop() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_STOP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SE426_Project_Car_sol_ArduinoCar load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SE426_Project_Car_sol_ArduinoCar(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SE426_Project_Car_sol_ArduinoCar load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SE426_Project_Car_sol_ArduinoCar(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SE426_Project_Car_sol_ArduinoCar load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SE426_Project_Car_sol_ArduinoCar(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SE426_Project_Car_sol_ArduinoCar load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SE426_Project_Car_sol_ArduinoCar(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SE426_Project_Car_sol_ArduinoCar> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SE426_Project_Car_sol_ArduinoCar.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SE426_Project_Car_sol_ArduinoCar> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SE426_Project_Car_sol_ArduinoCar.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SE426_Project_Car_sol_ArduinoCar> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SE426_Project_Car_sol_ArduinoCar.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SE426_Project_Car_sol_ArduinoCar> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SE426_Project_Car_sol_ArduinoCar.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ReturnResultEventResponse {
        public Log log;

        public String owner;

        public String _command;
    }
}
