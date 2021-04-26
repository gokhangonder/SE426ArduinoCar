To generate .bin .abi files:

solcjs SE426_Project_Car.sol --bin --abi --optimize -o se426

To generate Java Class from .bin .abi files:

web3j solidity generate -b SE426_Project_Car_sol_ArduinoCar.bin -a SE426_Project_Car_sol_ArduinoCar.abi -o java -p com.gokhangonder.se426arduino

