'use strict';
angular.module('myApp.serialColumn', ['ngRoute'])
        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/serial-column', {
                    templateUrl: 'serial-column/view.html',
                    controller: 'SerialColumnController'
                });
            }])
        .controller('SerialColumnController', function ($scope) {

            $scope.columnStacked = {
                "type": "serial",
                "categoryField": "category",
                "startDuration": 1,
                "categoryAxis": {
                    "gridPosition": "start"
                },
                "trendLines": [],
                "graphs": [
                    {
                        "balloonText": "[[title]] of [[category]]:[[value]]",
                        "fillAlphas": 1,
                        "id": "AmGraph-1",
                        "title": "graph 1",
                        "type": "column",
                        "valueField": "column-1"
                    },
                    {"balloonText": "[[title]] of [[category]]:[[value]]",
                        "fillAlphas": 1, "id": "AmGraph-2",
                        "title": "graph 2",
                        "type": "column",
                        "valueField": "column-2"
                    }
                ],
                "guides": [],
                "valueAxes": [
                    {
                        "id": "ValueAxis-1",
                        "stackType": "regular",
                        "title": "Axis title"
                    }
                ],
                "allLabels": [],
                "balloon": {},
                "legend": {
                    "useGraphSettings": true
                },
                "titles": [
                    {
                        "id": "Title-1",
                        "size": 15,
                        "text": "Chart Title"
                    }
                ],
                "data": [
                    {
                        "category": "category 1",
                        "column-1": 8,
                        "column-2": 5
                    },
                    {
                        "category": "category 2",
                        "column-1": 6,
                        "column-2": 7
                    },
                    {
                        "category": "category 3",
                        "column-1": 2,
                        "column-2": 3
                    }
                ]
            };

        });