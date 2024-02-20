package org.zerock.springex.sample;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@ToString
@Service
@RequiredArgsConstructor//생성자
public class SampleService {

    private final SampleDAO sampleDAO;
}
