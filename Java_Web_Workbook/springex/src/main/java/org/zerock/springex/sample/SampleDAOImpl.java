package org.zerock.springex.sample;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
//@Primary    //우선순위
@Qualifier("normal")
public class SampleDAOImpl implements SampleDAO{
}
