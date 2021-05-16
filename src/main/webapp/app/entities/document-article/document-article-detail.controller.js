(function() {
    'use strict';

    angular
        .module('pfeApp')
        .controller('Document_articleDetailController', Document_articleDetailController);

    Document_articleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Document_article'];

    function Document_articleDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Document_article) {
        var vm = this;

        vm.document_article = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pfeApp:document_articleUpdate', function(event, result) {
            vm.document_article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
