package telran.java51.pulse.servise;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import telran.java51.pulse.dto.PulseDto;

@Configuration
@RequiredArgsConstructor
public class PulseService {
	final StreamBridge streamBridge;
	
	@Value("${minpulse}")
	private String minpulse;
	
	@Value("${maxpulse}")
	private String maxpulse;
	
	@Bean
	Consumer<PulseDto> dispatchData(){
		return data -> {
			
			if(data.getPayload() < Integer.parseInt(minpulse)) {
				streamBridge.send("lowPulse-out-0", data);
				return;
			}
			if(data.getPayload() > Integer.parseInt(maxpulse)) {
				streamBridge.send("highPulse-out-0", data);
				return;
			}
			long delay = System.currentTimeMillis() - data.getTimestamp();
			System.out.println("delay:  " + delay + ", id: " + data.getId() + ", pulse: " + data.getPayload());
		};
	}

}
