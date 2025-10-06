async function loadWeather() {
    const city = document.getElementById('city').value;
    const res = await fetch(`/weather?city=${encodeURIComponent(city)}`);
    const data = await res.json();

    const ctx = document.getElementById('chart').getContext('2d');
    const temps = data.hourly.temperature_2m;
    const times = data.hourly.time;

    new Chart(ctx, {
        type: 'line',
        data: {
            labels: times,
            datasets: [{
                label: `Температура в ${city}`,
                data: temps,
                borderWidth: 2,
                borderColor: 'rgb(75, 192, 192)',
                fill: false
            }]
        },
        options: {
            scales: {
                x: { display: true, title: { display: true, text: 'Время' } },
                y: { display: true, title: { display: true, text: '°C' } }
            }
        }
    });
}