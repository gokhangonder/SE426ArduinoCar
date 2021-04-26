// Gökhan GÖNDER
// Gizem TURAN
// Rıza Berkay DELİKTAŞ
// SE426 - Arduino Car Project Sprint 5

/*
: Smart contract; Write a smart contract on Ethereum test environment Rinkeby
using Solidity language; The smart contract will have the following methods:
o Drive(): This command will mean to go forward for the robot car. 
o Stop(): This command will mean to stop for the robot car.
o Right(): This command will mean to turn right for the robot car.
o Left(): This command will mean to turn left for the robot car.
o Get(): This method will get the last given command, and return the related one of 
the following strings: “Drive”, “Stop”, “Right”, “Left”.
*/

pragma solidity ^0.8.2;

contract ArduinoCar {
    address owner;
    string private _drive = "Drive";
    string private _stop = "Stop";
    string private _left = "Left";
    string private _right = "Right";
    string private _lastCommand;
    
    event ReturnResult(address owner, string _command);
    
    constructor() public {
        _lastCommand = _stop;
        owner = msg.sender;
    }

    function Drive() public{
           _lastCommand = _drive;
    }

    function Left() public{
           _lastCommand = _left;
    }

    function Right() public{
           _lastCommand = _right;
    }

    function Stop() public{
          _lastCommand = _stop;
    }

    function Get() public returns (string memory result){
    
        emit ReturnResult(msg.sender, _lastCommand);
        return _lastCommand;
    }
}