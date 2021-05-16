'use strict';

describe('Controller Tests', function() {

    describe('Reglement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReglement, MockDepense, MockMode_de_paiment, MockCondition_de_reglement, MockArticle, MockFacture;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReglement = jasmine.createSpy('MockReglement');
            MockDepense = jasmine.createSpy('MockDepense');
            MockMode_de_paiment = jasmine.createSpy('MockMode_de_paiment');
            MockCondition_de_reglement = jasmine.createSpy('MockCondition_de_reglement');
            MockArticle = jasmine.createSpy('MockArticle');
            MockFacture = jasmine.createSpy('MockFacture');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Reglement': MockReglement,
                'Depense': MockDepense,
                'Mode_de_paiment': MockMode_de_paiment,
                'Condition_de_reglement': MockCondition_de_reglement,
                'Article': MockArticle,
                'Facture': MockFacture
            };
            createController = function() {
                $injector.get('$controller')("ReglementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pfeApp:reglementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
