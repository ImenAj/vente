(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Bons_de_livrisonController', Bons_de_livrisonController);

    Bons_de_livrisonController.$inject = ['Bons_de_livrison', 'Bons_de_livrisonSearch', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function Bons_de_livrisonController(Bons_de_livrison, Bons_de_livrisonSearch, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.bons_de_livrisons = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.clear = clear;
        vm.loadAll = loadAll;
        vm.search = search;

        loadAll();
        
        
        
        
        var app = angular.module('fileUpload', ['ngFileUpload']);

        app.controller('MyCtrl', ['$scope', 'Upload', function ($scope, Upload) {
            // upload later on form submit or something similar
            $scope.submit = function() {
              if ($scope.form.file.$valid && $scope.file) {
                $scope.upload($scope.file);
              }
            };

            // upload on file select or drop
            $scope.upload = function (file) {
                Upload.upload({
                    url: 'upload/url',
                    data: {file: file, 'username': $scope.username}
                }).then(function (resp) {
                    console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
                }, function (resp) {
                    console.log('Error status: ' + resp.status);
                }, function (evt) {
                    var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                    console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
                });
            };
            // for multiple files:
            $scope.uploadFiles = function (files) {
              if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                  Upload.upload({..., data: {file: files[i]}, ...})...;
                }
                // or send them all together for HTML5 browsers:
                Upload.upload({..., data: {file: files}, ...})...;
              }
            }
        

        function loadAll () {
            if (vm.currentSearch) {
                Bons_de_livrisonSearch.query({
                    query: vm.currentSearch,
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                Bons_de_livrison.query({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.bons_de_livrisons.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.bons_de_livrisons = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        function clear () {
            vm.bons_de_livrisons = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.searchQuery = null;
            vm.currentSearch = null;
            vm.loadAll();
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.bons_de_livrisons = [];
            vm.links = {
                last: 0
            };
            vm.page = 0;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.loadAll();
        }
    }
})();
