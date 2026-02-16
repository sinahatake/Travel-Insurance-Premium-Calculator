package org.javaguru.blacklist.core.api.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javaguru.blacklist.core.api.dto.BlackListedPersonDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlackListedPersonCoreCommand {

    private BlackListedPersonDTO personDTO;

}
