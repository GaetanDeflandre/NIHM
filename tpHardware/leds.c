#include <gpio.h>
#include <FreeRTOS.h>
#include <task.h>
#include <timer.h>

pin_t pin;

int main() {

	pin = make_pin(GPIO_PORT_B, 4);
	gpio_config(pin, pin_dir_write, pull_down);

	int period = 32768;
	int prescale = 1;

	timer_init(3, 1, prescale, period);

	int i = 0;
	double decal = 1;

	while(1) {
		timer_init_pwmchannel(3, 1, pin, i);
		i = i + decal;
		decal = (i == period || i == 0) ? -1 * decal : decal;
	}

	return 0;

}
