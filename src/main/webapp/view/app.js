var app = angular.module("app", ["apexcharts"])
app.service('StockService', ['$http', function ($http) {

    this.getStockData = function getStockData(data) {
        return $http.post('/getData', data, {
            headers: {
                'Content-Type': 'application/json; charset=utf-8'
            }
        });
    }
}
]);
app.controller('StockController', ['$scope', 'StockService', '$filter', function ($scope, StockService, $filter) {
    $scope.startDate = undefined;
    $scope.endDate = undefined;
    $scope.stockTinkerName = undefined;
    $scope.simpleMovingAverage = undefined;
    $scope.day = undefined;
    $scope.simpleMovingAverageMessage = "";
    $scope.chart = {
        chart: {
            type: 'candlestick',
            height: 1000
        },
        title: {
            text: '',
            align: 'left'
        },
        xaxis: {
            type: 'datetime',
            labels: {
                format: 'MM/yyyy',
            },
            tickPlacement: 'between'
        },
        yaxis: {
            tooltip: {
                enabled: true
            }
        },
        series: [
            {
                name: "candle",
                data: [
                    {
                        x: new Date(1538778600000),
                        y: [6629.81, 6650.5, 6623.04, 6633.33]
                    },
                ]
            }]
    };

    $scope.bar = {
        chart: {
            height: 350,
            type: 'bar',
            zoom: {
                enabled: false
            }
        },
        dataLabels: {
            enabled: false
        },
        stroke: {
            curve: 'straight'
        },
        series: [{
            name: "",
            data: [10]
        }],
        title: {
            text: 'Product Trends by Month',
            align: 'left'
        },
        grid: {
            row: {
                colors: ['#f3f3f3', 'transparent'],
                opacity: 0.5
            },
        },
        xaxis: {
            type: 'category',
            categories: [''],
            labels: {
                format: 'MM/yyyy',
            }
        }
    };

    $scope.updateData = function () {
        $scope.chart.series[0].data = [];
        $scope.bar.series[0].data = [];
        $scope.bar.xaxis.categories = [];
        $scope.startDate = $filter('date')($scope.startDate, 'yyyy-MM-dd');
        $scope.endDate = $filter('date')($scope.endDate, 'yyyy-MM-dd');
        var data = {
            stockTinkerName: $scope.stockTinkerName,
            startDate: $scope.startDate,
            endDate: $scope.endDate,
            collapse: $scope.collapse,
            daySize: $scope.day
        }

        StockService.getStockData(data).then(function (response) {
            $scope.bar.title.text = response.data.dataset.name;
            $scope.simpleMovingAverage = response.data.simpleMovingAverageList;
            $scope.simpleMovingAverageMessage = response.data.simpleMovingAverageMessage;
            for (var i = 0; i < response.data.dataset.data.length; i++) {
                var dataEntry = {
                    x: '',
                    y: []
                };
                const stockInfo = response.data.dataset.data[i];
                dataEntry.x = (new Date(stockInfo[0].replace('-', '/'))).getTime();
                for (var j = 1; j <= 4; j++) {
                    dataEntry.y.push(stockInfo[j]);
                }
                $scope.chart.series[0].data.push(dataEntry);
                $scope.bar.xaxis.categories.push(stockInfo[0]);
                $scope.bar.series[0].data.push(stockInfo[5]);
            }
        }, function (error) {
            console.log(error);
        });

    }
}]);